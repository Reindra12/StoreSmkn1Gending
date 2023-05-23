package com.example.storesmkn1gending.ui.detailProduct

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.storesmkn1gending.R
import com.example.storesmkn1gending.databinding.FragmentDetailProductBinding
import com.example.storesmkn1gending.databinding.FragmentHomeBinding
import com.example.storesmkn1gending.network.ImagesAsynctask
import com.example.storesmkn1gending.utils.SharedPrefs

class DetailProductFragment : Fragment() {

    private lateinit var sharedPrefs: SharedPrefs

    companion object {
        fun newInstance() = DetailProductFragment()
    }

    private lateinit var viewModel: DetailProductViewModel
    private var _binding: FragmentDetailProductBinding? = null
    private val binding get() = _binding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDetailProductBinding.inflate(inflater, container, false)
        val root: View = binding!!.root
        return root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        sharedPrefs = SharedPrefs(requireContext())
        var accesToken = sharedPrefs.accessToken
        viewModel = ViewModelProvider(this).get(DetailProductViewModel::class.java)
        val id = requireActivity().intent.getStringExtra("id")
        val productName = requireActivity().intent.getStringExtra("name")
        val discription = requireActivity().intent.getStringExtra("discription")
        val image = requireActivity().intent.getStringExtra("image")
        val location = requireActivity().intent.getStringExtra("location")

        binding!!.productTv.text = productName
        val imageLoaderTask = ImagesAsynctask(binding!!.detailImageview)
        imageLoaderTask.execute(image)
        binding!!.discTv.text = discription
        binding!!.locationTv.text = location

//        if (accesToken != null) {
//            viewModel.fetchAddMyCart(accesToken, id.toString())
//        }

    }

}