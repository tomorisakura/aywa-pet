package com.grevi.aywapet.ui.login.register

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.navArgs
import com.grevi.aywapet.R
import com.grevi.aywapet.databinding.FragmentRegisBinding
import com.grevi.aywapet.ui.home.HomeActivity
import com.grevi.aywapet.ui.viewmodel.RegisterViewModel
import com.grevi.aywapet.utils.RegisHelper
import com.grevi.aywapet.utils.Resource
import com.grevi.aywapet.utils.State
import com.grevi.aywapet.utils.snackBar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect

@AndroidEntryPoint
class RegisFragment : Fragment(), RegisHelper {

    private lateinit var binding : FragmentRegisBinding
    private val args : RegisFragmentArgs by navArgs()

    private val registerViewModel : RegisterViewModel by viewModels()
    private val TAG = RegisFragment::class.java.simpleName

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentRegisBinding.inflate(inflater)
        registerViewModel.regisHelper = this
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initBind()
        initButton()
    }

    private fun initBind() = with(binding) {
        val provinceMap = HashMap<String, String>()
        val provinceList = ArrayList<String>()

        val districtMap = HashMap<String, String>()
        val districtList = ArrayList<String>()

        val subDistrictMap = HashMap<String, String>()
        val subDistrictList = ArrayList<String>()

        val provinceSpinner = (txlProvince.editText as AutoCompleteTextView)
        val districtSpinner = (txlKabupaten.editText as AutoCompleteTextView)
        val subDistrictSpinner = (txlKecamatan.editText as AutoCompleteTextView)

        txlName.editText?.setText(args.name)

        registerViewModel.province.observe(viewLifecycleOwner, { resp ->
            when(resp.status) {
                Resource.STATUS.LOADING -> snackBar(binding.root, resp.msg!!).show()
                Resource.STATUS.ERROR -> snackBar(binding.root, resp.msg!!).show()
                Resource.STATUS.EXCEPTION -> snackBar(binding.root, resp.msg!!).show()
                Resource.STATUS.SUCCESS -> {
                    provinceList.clear()
                    resp.data?.result?.forEach {
                        provinceMap[it.id] = it.name
                        provinceList.add(it.name)
                    }
                    val adapter = ArrayAdapter(requireContext(), R.layout.list_item_spinner, R.id.tv_spinner, provinceList)
                    provinceSpinner.setAdapter(adapter)
                }
            }
        })

        provinceSpinner.setOnItemClickListener { _, _, position, _ ->
            provinceMap.filterValues { it == provinceList[position] }.keys.map {
                registerViewModel.getDistrict(it).observe(viewLifecycleOwner, { resp ->
                    when(resp.status) {
                        Resource.STATUS.LOADING -> snackBar(binding.root, resp.msg!!).show()
                        Resource.STATUS.ERROR -> snackBar(binding.root, resp.msg!!).show()
                        Resource.STATUS.EXCEPTION -> snackBar(binding.root, resp.msg!!).show()
                        Resource.STATUS.SUCCESS -> {
                            districtList.clear()
                            resp.data?.result?.forEach { data ->
                                districtMap[data.id] = data.name
                                districtList.add(data.name)
                            }

                            val adapter = ArrayAdapter(requireContext(), R.layout.list_item_spinner, R.id.tv_spinner, districtList)
                            districtSpinner.setAdapter(adapter)
                        }
                    }
                })
            }
        }

        districtSpinner.setOnItemClickListener { _, _, position, _ ->
            districtMap.filterValues { it == districtList[position] }.keys.map {
                registerViewModel.getSubDistrict(it).observe(viewLifecycleOwner, { resp ->
                    when(resp.status) {
                        Resource.STATUS.LOADING -> snackBar(binding.root, resp.msg!!).show()
                        Resource.STATUS.ERROR -> snackBar(binding.root, resp.msg!!).show()
                        Resource.STATUS.EXCEPTION -> snackBar(binding.root, resp.msg!!).show()
                        Resource.STATUS.SUCCESS -> {
                            subDistrictList.clear()
                            resp.data?.result?.forEach { data ->
                                subDistrictMap[data.id] = data.name
                                subDistrictList.add(data.name)
                            }

                            val adapter = ArrayAdapter(requireContext(), R.layout.list_item_spinner, R.id.tv_spinner, subDistrictList)
                            subDistrictSpinner.setAdapter(adapter)
                        }
                    }
                })
            }
        }

        txlName.editText?.addTextChangedListener(object : TextWatcher{
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                txlName.error = null
            }

            override fun afterTextChanged(s: Editable?) {
            }

        })

        txlNoHp.editText?.addTextChangedListener(object : TextWatcher{
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                txlNoHp.error = null
            }

            override fun afterTextChanged(s: Editable?) {
            }

        })

        txlAddress.editText?.addTextChangedListener(object : TextWatcher{
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                txlAddress.error = null
            }

            override fun afterTextChanged(s: Editable?) {
            }
        })

        txlProvince.editText?.addTextChangedListener(object : TextWatcher{
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                txlProvince.error = null
            }

            override fun afterTextChanged(s: Editable?) {
            }
        })

        txlKabupaten.editText?.addTextChangedListener(object : TextWatcher{
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                txlKabupaten.error = null
            }

            override fun afterTextChanged(s: Editable?) {
            }
        })

        txlKecamatan.editText?.addTextChangedListener(object : TextWatcher{
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                txlKecamatan.error = null
            }

            override fun afterTextChanged(s: Editable?) {
            }
        })

    }

    private fun initButton() = with(binding) {
        btnCreate.setOnClickListener {
            val name = txlName.editText?.text
            val phone = txlNoHp.editText?.text
            val province = txlProvince.editText?.text
            val district = txlKabupaten.editText?.text
            val subDistrict = txlKecamatan.editText?.text
            val street = txlAddress.editText?.text
            val address = "$street, $subDistrict, $district, $province"

            val email = args.email
            val uid = args.uid

            when {
                name.isNullOrEmpty() -> txlName.error = "field nama tidak boleh Kosong"
                phone.isNullOrEmpty() -> txlNoHp.error = "field no.hp tidak boleh kosong"
                province.isNullOrEmpty() -> txlProvince.error = "field provinsi tidak boleh kosong"
                district.isNullOrEmpty() -> txlKabupaten.error = "field kabupaten tidak boleh kosong"
                subDistrict.isNullOrEmpty() -> txlKecamatan.error = "field kecmatan tidak boleh kosong"
                street.isNullOrEmpty() -> txlAddress.error = "field alamat tidak boleh kosong"
                else -> registerViewModel.createUser(name.toString(), phone.toString(), address, email, uid)
            }
        }
    }

    override fun success() {
        Intent(activity, HomeActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
            startActivity(this)
            activity?.finish()
        }
    }

    override fun message(msg: String) {
        snackBar(binding.root, msg).show()
    }
}