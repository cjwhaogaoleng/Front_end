package com.example.learnoftheme.module.keyboard

import android.annotation.SuppressLint
import android.os.*
import android.os.Handler.Callback
import android.os.Handler.createAsync
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.learnoftheme.R
import com.example.learnoftheme.bean.DataBean
import com.example.learnoftheme.databinding.FragmentKeyboardBinding
import com.example.learnoftheme.module.maintwo.MainSharedViewModel
import com.google.gson.Gson
import okhttp3.FormBody
import okhttp3.OkHttpClient
import okhttp3.Request
import retrofit2.Retrofit
import java.util.*
import kotlin.concurrent.thread

private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

@SuppressLint("StaticFieldLeak")
private lateinit var binding: FragmentKeyboardBinding


/**
 * A simple [Fragment] subclass.
 * Use the [KeyboardFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class KeyboardFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    private lateinit var viewModel: MainSharedViewModel


    var i = 1

    private var input = ""
    private var output = ""

    //存放（）括号里的数
    private var inputBuffer = ""

    //是否可以点击运算符
    private var canOpe = true

    //记录是否已经计算了结果
    private var calculateFlag = false

    //记录当前切换到哪组按键 初始为sin类
    private var _2ndFlag = false

    //记录当前使用角度还是弧度
    private var isDEG = true
    private var deg = "yes"

    //记录刚刚是否使用了有括号的算法
    private var isBracket = false

    //判断是否右移
    private var ifRight = false


    private val TAG = "KeyboardFragment"


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
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_keyboard, container, false)
        binding.lifecycleOwner = this
        binding.kbClick = this


        viewModel = ViewModelProvider(requireActivity()).get<MainSharedViewModel>(
            MainSharedViewModel::class.java
        )
        return binding.root
    }

    companion object {
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            KeyboardFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }

    //33
    @RequiresApi(Build.VERSION_CODES.P)
    fun click(view: View) {
        try {
            Toast.makeText(view.context,"csh大坏蛋",Toast.LENGTH_SHORT).show()
            //10
            when ((view as Button).text) {
                binding.bt0.text, binding.bt1.text, binding.bt2.text,
                binding.bt3.text, binding.bt4.text, binding.bt5.text,
                binding.bt6.text, binding.bt7.text, binding.bt8.text,
                binding.bt9.text, ".", "π", "e" -> {
                    if (isBracket) {
                        input = input.substring(0,input.length-1)
                        input += (view).text
                        input += ")"
                    }

                    else input += (view).text
//                    inputBuffer += view.text
                    canOpe = true
                }
                //4
                "+", "-", "×", "➗" -> {
                    //说明之前一直在输入数字
                    if (canOpe) {
                        input += (view).text
                        isBracket = false
                        canOpe = false
                    }
                }

                //3
                binding.btEqual.text -> {
                    //更新计算结果
                    getResult(view, deg)

                    //更新历史记录
                    getHistory()
                    calculateFlag = true

                }

                binding.btClear.text -> {
                    input = ""
                }

                binding.btDelete.text -> {
                    input = input.substring(0, input.length - 1)
                }

                //7
                binding.btMod.text -> {
                    input += "mod"
                }

                binding.btFactorial.text -> {
                    input += "!"
                }
                binding.btReciprocal.text -> {
                    input+="^-1"
                }

                "1/x" -> {
                    input += "^-1"
                }
                "|x|" -> {
                    input += "abs()"
                    isBracket = true
                }
                "(" -> {
                    input += "("
                }
                ")" -> {
                    input += ")"
                }
                "+/-" -> {
                    Toast.makeText(view.context,"no function",Toast.LENGTH_SHORT).show()

                }
                //2
                //DEG角度制
                //RAD弧度制
                "DEG" -> {
                    binding.btDEG.text = "RAD"
                    deg = "no"
                    isDEG = false
                }
                "RAD" -> {
                    binding.btDEG.text = "DEG"
                    deg = "yes"
                    isDEG = true
                }

                //9
                binding.bt2nd.text -> {
                    if (!_2ndFlag) {
                        binding.btSin.text = "sin"
                        binding.btCos.text = "cos"
                        binding.btTan.text = "tan"

                        binding.btSqrt.text = "2√x"
                        binding.btSquare.text = "x^2"
                        binding.btPower.text = "x^y"
                        binding.bt10Power.text = "10^x"
                        binding.btLog.text = "log"
                        binding.btLn.text = "ln"
                        _2ndFlag = true
                    } else {
                        binding.btSin.text = "asin"
                        binding.btCos.text = "acos"
                        binding.btTan.text = "atan"

                        binding.btSqrt.text = "3√x"
                        binding.btSquare.text = "x^3"
                        binding.btPower.text = "y√x"
                        binding.bt10Power.text = "2^x"
                        binding.btLog.text = "logy(x)"
                        binding.btLn.text = "e^x"

                        _2ndFlag = false
                    }
                }

                "sin" -> {
                    input += "sin()"
                    isBracket = true
                }

                "cos" -> {
                    input += "cos()"
                    isBracket = true
                }

                "tan" -> {
                    input += "tan()"
                    isBracket = true

                }

                "asin" -> {
                    input += "asin()"
                    isBracket = true
                }

                "acos" -> {
                    input += "acos()"
                    isBracket = true

                }

                "atan" -> {
                    input += "atan()"
                    isBracket = true

                }
                "2√x" -> {
                    input+="^1/2"
                }
                "3√x" -> {
                    input+="^1/3"
                }
                "x^2" -> {
                    input+="^2"
                }
                "x^3" -> {
                    input+="^3"

                }
                "x^y" -> {
                    input += "^"
                }
                "y√x" -> {
                    input += "√"
                }
                "10^x" -> {
                    input += "10^"
                    canOpe = false

                }
                "2^x" -> {
                    input+="2^"
                    canOpe = false

                }
                "log" -> {
                    input += "log"
                    canOpe = false
                }
                "logy(x)" -> {
                    input += "log()"
                    canOpe = false
                }
                "ln" -> {
                    input += "ln"
                    canOpe = false
                }
                "e^x" -> {
                    input += "e^"
                    canOpe = false
                }


                binding.btMove.text->{

                }
            }
        } catch (e: java.lang.Exception) {

        } finally {

            if(input!="")viewModel.input.value = input
            else viewModel.input.value = " "
            if (calculateFlag) {
                input = output.toString()
                calculateFlag = false
            }
        }
    }

    private fun getHistory()  {
        val okHttpClient = OkHttpClient.Builder()
            .build()

        val body = FormBody.Builder()
            .build()

        val request =
            Request.Builder().url("http://8.130.110.171:80/read_all_history/").post(body)
                .build()

        val call = okHttpClient?.newCall(request)

        var bean: DataBean = DataBean()
        Thread {
            val response = call?.execute()
            bean =
                Gson().fromJson<DataBean>(response!!.body()!!.string(), DataBean::class.java)
            activity?.runOnUiThread {
                viewModel.history.value =
                    bean.result.split(",")
            }

        }.start()
        println(bean.result)

    }


    @RequiresApi(Build.VERSION_CODES.P)
    private fun getResult(view: View, deg: String) {

        val okHttpClient = OkHttpClient.Builder()
            .build()

        val body = FormBody.Builder().add("formula", input)
            .add("deg", deg)
            .build()

        val request =
            Request.Builder().url("http://8.130.110.171:80/get_result/").post(body)
                .build()

        val call = okHttpClient?.newCall(request)

        var bean: DataBean = DataBean()
        var thread = Thread {
            val response = call?.execute()

            bean =
                Gson().fromJson<DataBean>(response!!.body()!!.string(), DataBean::class.java)

            activity?.runOnUiThread {
                viewModel.result.value =
                    bean.result
            }
        }.start()

    }

}
