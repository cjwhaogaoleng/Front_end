# Front_end
1. 布局介绍
    1. 主要activity中
       
       ```xml
       <LinearLayout
        android:layout_width="match_parent"
        android:layout_height="match_parent"
        android:orientation="vertical"
        android:padding="20dp"
        tools:context=".module.maintwo.MainActivityTwo">

        <TextView
            android:id="@+id/tv_process"
            android:layout_width="match_parent"
            android:layout_height="50dp"
            android:layout_marginTop="10sp"
            android:gravity="center_vertical"
            android:textColor="@color/black"
            android:textSize="30dp" />

        <TextView
            android:id="@+id/tv_result"
            android:layout_width="match_parent"
            android:layout_height="50dp"
            android:layout_marginTop="20dp"
            android:gravity="center_vertical"
            android:textColor="@color/black"
            android:textSize="30dp" />

        <androidx.viewpager2.widget.ViewPager2
            android:id="@+id/vp_main"
            android:layout_width="match_parent"
            android:layout_height="0dp"
            android:layout_marginTop="20dp"
            android:layout_weight="1"
            android:paddingTop="30sp"
            android:paddingBottom="30sp"/>
      </LinearLayout>
      ```
在viewpager2中嵌套我们的计算器和历史记录来实现滑动效果

    2. 计算器
    ```xml
    <LinearLayout
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        android:orientation="vertical"
        tools:context=".module.keyboard.KeyboardFragment">

        <LinearLayout
            android:layout_width="match_parent"
            android:layout_height="0dp"
            android:layout_weight="1"
            android:orientation="horizontal">

            <Button
                android:id="@+id/bt_2nd"
                android:layout_width="0dp"
                android:layout_height="wrap_content"
                android:layout_weight="1"
                android:onClick="@{(view)->kb_click.click(view)}"
                android:text="2nd"
                android:textAllCaps="false" />
            <Button
                android:id="@+id/bt_sin"
                android:layout_width="0dp"
                android:layout_height="wrap_content"
                android:layout_weight="1"
                android:onClick="@{(view)->kb_click.click(view)}"
                android:text="sin"
                android:textAllCaps="false" />
            <Button
                android:id="@+id/bt_cos"
                android:layout_width="0dp"
                android:layout_height="wrap_content"
                android:layout_weight="1"
                android:onClick="@{(view)->kb_click.click(view)}"
                android:text="cos"
                android:textAllCaps="false" />
            <Button
                android:id="@+id/bt_tan"
                android:layout_width="0dp"
                android:layout_height="wrap_content"
                android:layout_weight="1"
                android:onClick="@{(view)->kb_click.click(view)}"
                android:text="tan"
                android:textAllCaps="false" />
            <Button
                android:id="@+id/bt_DEG"
                android:layout_width="0dp"
                android:layout_height="wrap_content"
                android:layout_weight="1"
                android:onClick="@{(view)->kb_click.click(view)}"
                android:text="DEG"
                android:textAllCaps="false" />

        </LinearLayout>
```
这是一行的布局，通过linearlayout线性布局即可


    3. 历史记录
       
       
    
       

    

```xml
<FrameLayout
        android:layout_width="match_parent"
        android:layout_height="match_parent"
        tools:context=".module.history.HistoryFragment">

        <ListView
            android:layout_margin="20sp"
            android:id="@+id/lv_history"
            android:layout_width="match_parent"
            android:layout_height="match_parent" />

    </FrameLayout>
```
只有listview
2. 前后端对接
    我们使用源生Okhttp
    ```kotlin
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
    ```
    这是异步获取计算结果的方法，以及利用Gson自己转换json数据，在进入主线程将数据共享
3. 数据共享
   采用viewModel完成activity和fragment的数据共享
   ```kotlin
class MainSharedViewModel : ViewModel() {

    var input:MutableLiveData<String> = MutableLiveData<String>()

    var history: MutableLiveData<List<String>> = MutableLiveData()

    var result: MutableLiveData<String> = MutableLiveData()
}
   ```
这里在viewModel中简单设置变量
在activity和fragment中获取该viewModel
通过为每个变量设计observer监听数据改变，并完成实时更新UI
```kotlin
viewModel.input.observe(this
            ) {
                println("input has changed to " +viewModel.input.value )

                binding.tvProcess.text = viewModel.input.value }
            viewModel.result.observe(this) {

                println("result has changed " + viewModel.result.value)
                binding.tvResult.text = viewModel.result.value
                binding.tvResult.text = viewModel.result.value

            }
```
