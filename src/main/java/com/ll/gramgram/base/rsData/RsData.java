package com.ll.gramgram.base.rsData;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class RsData<T> {
    // 제네릭 타입을 사용하여 결과 데이터를 나타내는 클래스

    private String resultCode; // 결과 코드

    private String msg; // 결과 메시지

    private T data; // 데이터

    public static <T> RsData<T> of(String resultCode, String msg, T data){
        return new RsData<>(resultCode, msg, data);
        // 결과데이터를 초기화하는 of메서드
    }

    public static <T> RsData<T> of(String resultCode, String msg){
        return of(resultCode, msg, null);
        // 데이터가 없는 경우 결과 데이터를 초기화하는 메서드
    }

    public static <T> RsData<T> successOf(T data){
        return of("S-1", "성공", data);
        // 성공 결과 데이터를 초기화하는 메서드
    }

    public static <T> RsData<T> failOf(T data){
        return of("F-1", "실패", data);
        // 실패 결과를 나타내는 메서드
    }

    // 성공 여부를 확인하는 메서드
    public boolean isSuccess(){
        return resultCode.startsWith("S-");
    }

    // 실패여부를 확인하는 메서드
    public boolean isFail(){
        return isSuccess() == false;
    }
}

