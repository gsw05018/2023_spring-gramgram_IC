package com.ll.gramgram.standard.util;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

public class Ut {
    public static class url {

        public static String encode(String str) {
            try {
                return URLEncoder.encode(str, "UTF-8");
            } catch (UnsupportedEncodingException e) {
                return str;
            }
        } // 문자열을 URL 인코딩하는 메서드

        public static String modifyQueryParam(String url, String paramName, String paramValue) {
            url = deleteQueryParam(url, paramName);
            // 기존 쿼리 매개변수 삭제
            url = addQueryParam(url, paramName, paramValue);
            // 새로운 쿼리 매개변수 추가

            return url;
        } // URL 쿼리 매개변수를 수정하는 메서드

        public static String addQueryParam(String url, String paramName, String paramValue) {
            if (url.contains("?") == false) {
                url += "?";
            }
            // URL에 ?가 포함되어 있지 않으면 ? 추가

            if (url.endsWith("?") == false && url.endsWith("&") == false) {
                url += "&";
            }
            // URL이 ?로 끝나지 않고 &로 끝나지 않으면 &를 추가

            url += paramName + "=" + paramValue;
            // 쿼리 매개변수를 URL에 추가

            return url;
        } // URL에 쿼리 매개변수를 추가하는 메서드

        private static String deleteQueryParam(String url, String paramName) {
            int startPoint = url.indexOf(paramName + "=");
            // URL에서 해당 쿼리 매개변수의 시작 위치를 찾음
            if (startPoint == -1) return url;

            int endPoint = url.substring(startPoint).indexOf("&");
            // 쿼리 매개변수의 끝 위치를 찾음

            if (endPoint == -1) {
                return url.substring(0, startPoint - 1);
            }
            // 쿼리 매개변수가 URL의 마지막이라면 해당 부분을 삭제

            String urlAfter = url.substring(startPoint + endPoint + 1);
            // URL에서 해당 쿼리 매개변수를 제외한 부분을 재구성하여 반환

            return url.substring(0, startPoint) + urlAfter;
        } // URL에서 특정 쿼리 매개변수를 제거하는 메서드
    }
}