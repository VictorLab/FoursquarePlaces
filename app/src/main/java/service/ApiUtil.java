package service;

import helper.Constants;

/**
 * Created by Victor on 2018/04/20.
 */

public class ApiUtil {


    public static FourSquareService getRetrofitService(){

        FourSquareService squareService = RetrofitInstance.getInstance(Constants.BASE_URL).create(FourSquareService.class);

        return squareService;
    }

}

