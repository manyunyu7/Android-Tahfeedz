package com.feylabs.tahfidz.View.Teacher

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.feylabs.tahfidz.R
import com.feylabs.tahfidz.Util.SharedPreference.Preference
import com.feylabs.tahfidz.View.BaseView.BaseFragment
import com.feylabs.tahfidz.ViewModel.QuizViewModel
import kotlinx.android.synthetic.main.fragment_mentor_add_quiz.*

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [MentorAddQuizFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class MentorAddQuizFragment : BaseFragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_mentor_add_quiz, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val quizViewModel =
            ViewModelProvider(requireActivity(), ViewModelProvider.NewInstanceFactory())
                .get(QuizViewModel::class.java)



        btnSubmitQuiz.setOnClickListener {
            var stat = true
            if (quizTitle.text.toString().isBlank()) {
                quizTitle.error = "Mohon isi bidang ini"
                stat = false
            }
            if (quizDesc.text.toString().isBlank()) {
                quizDesc.error = "Mohon isi bidang ini"
                stat = false
            }
            if (quizLink.text.toString().isBlank()) {
                quizLink.error = "Mohon isi bidang ini"
                stat = false
            }
            if (stat) {
                quizViewModel.insertQuiz(
                    title = quizTitle.text.toString(),
                    id = Preference(requireContext()).getPrefString("group_temp").toString(),
                    gform_link = quizLink.text.toString(),
                    desc = quizDesc.text.toString()
                )
            }
        }

        quizViewModel.quizInsertStatus.observe(viewLifecycleOwner, Observer {
            if (it == 3) {
                anim_loading.visibility = View.VISIBLE
            }
            if (it == 1) {
                "Berhasil Menambah Quiz".showToast()
                anim_loading.visibility = View.GONE
            }
            if (it == 2) {
                "Gagal Menambah Quiz".showToast()
                anim_loading.visibility = View.GONE
            }
            if (it == 0) {
                "Terjadi Gangguan Koneksi".showToast()
                anim_loading.visibility = View.GONE
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
         * @return A new instance of fragment MentorAddQuizFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            MentorAddQuizFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}