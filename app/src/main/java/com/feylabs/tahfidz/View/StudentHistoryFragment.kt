package com.feylabs.tahfidz.View

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.feylabs.tahfidz.Model.SubmissionAdapter
import com.feylabs.tahfidz.R
import com.feylabs.tahfidz.Util.SharedPreference.Preference
import com.feylabs.tahfidz.View.Base.BaseFragment
import com.feylabs.tahfidz.ViewModel.SubmissionViewModel
import kotlinx.android.synthetic.main.fragment_student_history.*

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [StudentHistoryFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class StudentHistoryFragment : BaseFragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    lateinit var submissionAdapter: SubmissionAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onResume() {
        super.onResume()
        val submissionViewModel =
            ViewModelProvider(this, ViewModelProvider.NewInstanceFactory()).get(SubmissionViewModel::class.java)
        submissionViewModel.retrieveSubmissionStudent(
            Preference(requireContext()).getPrefString("student_id").toString()
        )

        submissionViewModel.dataSubmission.observe(viewLifecycleOwner, Observer { list->
            anim_loading.visibility=View.GONE
            if (list!=null){
                submissionAdapter = SubmissionAdapter()
                submissionAdapter.listData.clear()
                submissionAdapter.setData(list)
                recycler_submission.setHasFixedSize(true)
                recycler_submission.layoutManager=(LinearLayoutManager(requireContext(),LinearLayoutManager.VERTICAL,false))
                recycler_submission.adapter=submissionAdapter
            }else{

            }

        })

    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val submissionViewModel =
            ViewModelProvider(this, ViewModelProvider.NewInstanceFactory()).get(SubmissionViewModel::class.java)

        submissionViewModel.retrieveSubmissionStudent(
            Preference(requireContext()).getPrefString("student_id").toString()
        )

        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_student_history, container, false)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        submissionAdapter = SubmissionAdapter()
        anim_loading.visibility=View.VISIBLE

        val submissionViewModel = ViewModelProvider(this, ViewModelProvider.NewInstanceFactory())
            .get(SubmissionViewModel::class.java)

        submissionViewModel.dataSubmission.observe(viewLifecycleOwner, Observer { list->
            anim_loading.visibility=View.GONE
            submissionAdapter.setData(list)
            recycler_submission.setHasFixedSize(true)
            recycler_submission.layoutManager=(LinearLayoutManager(requireContext(),LinearLayoutManager.VERTICAL,false))
            recycler_submission.adapter=submissionAdapter

        })

        submissionViewModel.status.observe(viewLifecycleOwner, Observer {
            if (it){
                anim_loading.visibility=View.GONE
            }else{
                anim_loading.visibility=View.GONE
            }
        })

    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment StudentHistoryFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            StudentHistoryFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}