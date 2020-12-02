package com.feylabs.tahfidz.View.SharedView

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.feylabs.tahfidz.Model.QuizAdapter
import com.feylabs.tahfidz.R
import com.feylabs.tahfidz.Util.SharedPreference.Preference
import com.feylabs.tahfidz.View.BaseView.BaseFragment
import com.feylabs.tahfidz.ViewModel.QuizViewModel
import kotlinx.android.synthetic.main.fragment_mentor_quiz.*
import kotlinx.android.synthetic.main.layout_loading_transparent.*

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [MentorQuizFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class HomeQuizFragment : BaseFragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    lateinit var quizViewModel: QuizViewModel
    lateinit var quizAdapter: QuizAdapter

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
        quizViewModel = ViewModelProvider(requireActivity(), ViewModelProvider.NewInstanceFactory())
            .get(QuizViewModel::class.java)

        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_mentor_quiz, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        quizAdapter = QuizAdapter()
        rv_quiz.setHasFixedSize(true)
        rv_quiz.layoutManager = LinearLayoutManager(requireContext())

        quizViewModel.getQuiz(Preference(requireContext()).getPrefString("group_temp").toString())

        quizViewModel.quizGetStatus.observe(viewLifecycleOwner, Observer {
            when (it) {
                3 -> {
                    anim_loading.visibility = View.VISIBLE
                    "Gagal Retrieve Data , Silakan Coba Lagi Nanti".showToast()
                }
                2->{
                    anim_loading.visibility = View.GONE
                    cfAlert("Belum Ada Data Tersedia",R.color.design_default_color_background,
                        R.color.colorWhite)
                }
                1 -> {
                    anim_loading.visibility = View.GONE
                    "Berhasil Retrieve Data".showToast()
                }
                0 -> {

                    anim_loading.visibility = View.GONE
                    "Gagal Retrieve Data , Periksa Koneksi Internet Anda dan Coba Lagi Nanti".showToast()
                }
            }
        })

        quizViewModel.listQuiz.observe(viewLifecycleOwner, Observer {
            if (it != null) {
                quizAdapter.addData(it)
                rv_quiz.adapter = quizAdapter
            } else {
                "Belum Ada Quiz Tersedia".showToast()
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
         * @return A new instance of fragment MentorQuizFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            HomeQuizFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}