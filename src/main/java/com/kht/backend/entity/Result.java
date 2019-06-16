package com.kht.backend.entity;

public class Result {
    private int statusCode;
    private String msg;
    private Object data;

    public Result(int statusCode, String msg, Object data){
        this.statusCode = statusCode;
        this.msg = msg;
        this.data = data;
    }

    public int getStatusCode(){
        return statusCode;
    }

    public String getMsg(){
        return msg;
    }

    public Object getData(){
        return data;
    }

    /**
     * 用ResultBuilder生成Result，static保证外部无法使用该类，且无法对Result进行修改
     * Result类无setter函数也是为了Result不可改变
     */
    public static class ResultBuilder{
        private int statusCode;
        private String msg;
        private Object data;

        public ResultBuilder(int statusCode, String msg){
            this.statusCode = statusCode;
            this.msg = msg;
        }

        public ResultBuilder(int statusCode, String msg, Object data){
            this.statusCode = statusCode;
            this.msg = msg;
            this.data = data;
        }

        public Result build(){
            return new Result(statusCode, msg, data);
        }

        public int getStatusCode(){
            return statusCode;
        }

        public String getMsg() {
            return msg;
        }

        public Object getData() {
            return data;
        }
    }

    public static ResultBuilder OK(Object data){
        return new ResultBuilder(200,"OK",data);
    }
}
