package com.rwAUiM.mjaymza0mdm2njqz.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.rwAUiM.mjaymza0mdm2njqz.R
import com.rwAUiM.mjaymza0mdm2njqz.utils.BarUtils
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlin.random.Random


/**
 * A simple [Fragment] subclass.
 * Use the [HomeFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class HomeFragment : Fragment() {
    var cl1 = 7000
    var cl2 = 8000
    val random = Random.Default
    private lateinit var tvTop: TextView
    private lateinit var tv1: TextView
    private lateinit var tv2: TextView
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        return inflater.inflate(R.layout.fragment_home, container, false)
    }


    fun updateValue() {
        val op = random.nextInt(2)
        if (op == 0) {
            cl1 += random.nextInt(1, 99)
            cl2 += random.nextInt(1, 99)
        } else {
            cl1 -= random.nextInt(1, 99)
            cl2 -= random.nextInt(1, 99)
        }
    }

    fun setText() {
        tvTop.text = "${cl1 + cl2}"
        tv1.text = "$cl1"
        tv2.text = "$cl2"
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        tvTop = view.findViewById(R.id.tv_sats_top)
        tv1 = view.findViewById(R.id.tv_sats1_top)
        tv2 = view.findViewById(R.id.tv_sats2_top)
        MainScope().launch(Dispatchers.IO){
            while (true) {
                updateValue()
                withContext(Dispatchers.Main) {
                    setText()
                }
                delay(1000)
            }
        }
        context?.let { BarUtils.setPaddingSmart(it, view.findViewById(R.id.title_bar)) }
    }
}