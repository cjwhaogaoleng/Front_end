package com.example.learnoftheme.bean

class DataBean() {
//    getSync: {
//        "message": "success",
//        "result": "Error: \u8bed\u6cd5\u9519\u8bef\uff0c\u8bf7\u6b63\u786e\u8f93\u5165"
//    }

    var message = ""
    var result = ""

    override fun toString(): String {
        return "DataBean(message='$message', result='$result')"
    }


}