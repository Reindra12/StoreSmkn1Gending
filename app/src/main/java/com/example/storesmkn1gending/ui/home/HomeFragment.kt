package com.example.storesmkn1gending.ui.home

import android.app.Activity
import android.app.SearchManager
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.core.content.ContextCompat.getSystemService
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.storesmkn1gending.R
import com.example.storesmkn1gending.databinding.FragmentHomeBinding
import com.example.storesmkn1gending.model.ResponseProductItem
import com.example.storesmkn1gending.ui.dashboard.ProductAdapter
import com.example.storesmkn1gending.ui.detailProduct.DetailProductActivity
import com.example.storesmkn1gending.ui.detailProduct.DetailProductFragment
import com.example.storesmkn1gending.ui.profile.ProfileFragment
import com.example.storesmkn1gending.utils.SharedPrefs
import java.util.Locale

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private lateinit var sharedPrefs: SharedPrefs
    private lateinit var productAdapter: ProductAdapter
    private lateinit var listFilterProduct: List<ResponseProductItem>

    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val homeViewModel =
            ViewModelProvider(this).get(HomeViewModel::class.java)

        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root

        productAdapter = ProductAdapter { product ->
            goToDetailProduct(product)
//            val actiontodetail =
        }

        binding.dashboardRv.apply {
            layoutManager = GridLayoutManager(requireContext(), 2)
            adapter = productAdapter
        }

        val productListObserver = Observer<List<ResponseProductItem>> { productList ->
            listFilterProduct = productList
//            productAdapter.submitList(productList)
            productAdapter.setFilteredList(listFilterProduct)

        }

        homeViewModel.productList.observe(viewLifecycleOwner, productListObserver)
        sharedPrefs = SharedPrefs(requireContext())
        homeViewModel.fetchProductList(sharedPrefs.accessToken.toString())

        filterData()

        return root
    }

    private fun goToDetailProduct(product: ResponseProductItem) {

        val bundle = Bundle().apply {
            putSerializable("product", product)

        }
//        val detailProductFragment = ProfileFragment()
//        detailProductFragment.arguments = bundle
//        val fragmentManager = requireActivity().supportFragmentManager
//        val fragmentTrasaction  = fragmentManager.beginTransaction()
//
//        fragmentTrasaction.addToBackStack(null)
//        fragmentTrasaction.replace(
//            R.id.nav_host_fragment_activity_bottom_navigation,
//            detailProductFragment
//        )
//        fragmentTrasaction.commit()
//
        val intent = Intent(context, DetailProductActivity::class.java).apply {
            putExtras(bundle)

        }

        requireContext().startActivity(intent)
    }

    private fun filterData() {
        val searchManager =
            requireContext().getSystemService(Context.SEARCH_SERVICE) as SearchManager

        binding.searchView.setSearchableInfo(
            searchManager.getSearchableInfo(requireActivity().componentName)
        )
        binding.searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                filterProductList(newText)
                return true
            }
        })


    }

    private fun filterProductList(newText: String?) {
        val filteredList = if (newText.isNullOrBlank()) {
            listFilterProduct
        } else {
            listFilterProduct.filter { product ->
                product.name.toLowerCase(Locale.getDefault())
                    .contains(newText.toLowerCase(Locale.getDefault()))
            }
        }
        productAdapter.setFilteredList(filteredList)
        binding.dashboardRv.scrollToPosition(0)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}