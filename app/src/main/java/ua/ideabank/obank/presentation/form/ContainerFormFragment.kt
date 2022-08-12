package ua.ideabank.obank.presentation.form

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import org.koin.androidx.viewmodel.ext.android.sharedViewModel
import ua.ideabank.obank.R
import ua.ideabank.obank.core.presentation.adapter.PagerAdapters
import ua.ideabank.obank.core.presentation.ui.base.BaseFragment
import ua.ideabank.obank.databinding.FragmentManagerFormBinding
import ua.ideabank.obank.presentation.form.model.TestDatumModel

class ContainerFormFragment : BaseFragment<FragmentManagerFormBinding, FormViewModel>()  {
    private val listInformationOne: ArrayList<TestDatumModel> = arrayListOf()
    private val listInformationTwo: ArrayList<TestDatumModel> = arrayListOf()

    private val listFragment = mutableListOf<Fragment>()

    override var layoutResId = R.layout.fragment_manager_form
    override val viewModel: FormViewModel by sharedViewModel()

    override fun configureView(savedInstanceState: Bundle?) {
        binding.vm = viewModel
        initView()
    }

    private fun initView() {
        setToolbarTitle(getString(R.string.form_title))
        initToolBar()

        val adapter = PagerAdapters(childFragmentManager)
        listFragment.add(FormFragment(listInformationOne))
        listFragment.add(FormFragment(listInformationTwo))

        listFragment.forEachIndexed { index, element ->
            if (getFragmentIndex(index)){
                adapter.addFragment(element)
            }
        }
        binding.formViePager.adapter = adapter

        binding.nextBtn.setOnClickListener {
            binding.formViePager.currentItem = 1
        }
    }

    private fun initToolBar(){
        (activity as FormActivity?)!!.setSupportActionBar(binding.toolbar.titleToolBar)
        (activity as FormActivity?)!!.supportActionBar?.let {
            it.setDisplayHomeAsUpEnabled(true)
            it.setDisplayShowCustomEnabled(true)
        }
        setHasOptionsMenu(true)
    }

    private fun setToolbarTitle(title: String) {
        binding.toolbar.titleToolBar.title = title
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        return inflater.inflate(R.menu.toolbar_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.info_item ->{
            }
            android.R.id.home ->{
                val l = binding.formViePager.currentItem
                if (l <= 0){
                    requireActivity().onBackPressed()
                }
                binding.formViePager.currentItem = 0
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun getFragmentIndex(index: Int): Boolean{
        var indexValue = false
        if (index != -1){
            if (index == 0){

            }else{

            }
            indexValue = true
        }else {
            indexValue = false
        }
        return indexValue
    }

//    private fun initTestModel() {
//        val massive = arrayOf("iнженер/спецiалiст",
//            "iнтелектуальний працiвник",
//            "безробітний",
//            "бухгалтер",
//            "вiйськовий/пожежник",
//            "водiй",
//            "вчитель/викладач")
//        val listButton = arrayOf("Ні", "Так", "Так")
//        val listButton1 = arrayOf("Ні")
//
//        val data1 = TestDatumModel(
//            "0", null, null,
//            "Посада або службове становище",
//            "select", true, true,
//            "hidden", null, null, massive)
//        listInformationOne.add(data1)
//
//        val data2 = TestDatumModel(
//            "1", null, null,
//            "",
//            "edit", true, false,
//            "hidden", "Назва організації, в якій ви працююте", null)
//        listInformationOne.add(data2)
//
//        val data3 = TestDatumModel(
//            "2", null, null,
//            "Ви зареєстровані як підприємець (ФОП)?",
//            "button", true, false,
//            "hidden", null, null, listButton1)
//        listInformationOne.add(data3)
//
//        val data4 = TestDatumModel(
//            "2", null, null,
//            "Ви зареєстровані як підприємець (ФОП)?",
//            "button", true, false,
//            "hidden", null, null, listButton)
//        listInformationOne.add(data4)
//    }
//
//    private fun initTestModel1() {
//        val massive = arrayOf("iнженер/спецiалiст",
//            "iнтелектуальний працiвник",
//            "безробітний",
//            "бухгалтер",
//            "вiйськовий/пожежник",
//            "водiй",
//            "вчитель/викладач")
//        val listButton = arrayOf("Ні", "Так")
//        val listButton1 = arrayOf("Ні", "Так")
//
//        val data1 = TestDatumModel(
//            "0", null, null,
//            "Посада або службове становище",
//            "select", true, true,
//            "hidden", null, null, massive)
//        listInformationTwo.add(data1)
//
//        val data2 = TestDatumModel(
//            "1", null, null,
//            "",
//            "edit", true, false,
//            "hidden", "Назва організації, в якій ви працююте", null)
//        listInformationTwo.add(data2)
//
//        val data5 = TestDatumModel(
//            "1", null, null,
//            "",
//            "edit", true, false,
//            "hidden", "Назва організації, в якій ви працююте", null)
//        listInformationTwo.add(data5)
//
//        val data3 = TestDatumModel(
//            "2", null, null,
//            "Ви зареєстровані як підприємець (ФОП)?",
//            "button", true, false,
//            "hidden", null, null, listButton1)
//        listInformationTwo.add(data3)
//
//        val data6 = TestDatumModel(
//            "2", null, null,
//            "Ви здійснююте незалежну проффесійну діяльність?",
//            "button", true, false,
//            "hidden", null, null, listButton1)
//        listInformationTwo.add(data6)
//
//        val data4 = TestDatumModel(
//            "2", null, null,
//            "Ви зареєстровані як підприємець (ФОП)?",
//            "button", true, false,
//            "hidden", null, null, listButton)
//        listInformationTwo.add(data4)
//    }
}