package info.gogou.gogou.utils;

import android.util.Log;

import com.octo.android.robospice.persistence.exception.SpiceException;
import com.octo.android.robospice.request.listener.RequestListener;

import info.gogou.gogou.R;
import info.gogou.gogou.app.GogouApplication;
import info.gogou.gogou.listeners.RESTRequestListener;
import info.gogou.gogou.model.Address;
import info.gogou.gogou.model.AddressList;
import info.gogou.gogou.model.DemandFilter;
import info.gogou.gogou.model.DemandList;
import info.gogou.gogou.model.OAuth2Login;
import info.gogou.gogou.model.Order;
import info.gogou.gogou.model.OrderFilter;
import info.gogou.gogou.model.OrderList;
import info.gogou.gogou.model.PaymentMethod;
import info.gogou.gogou.model.PaymentMethodList;
import info.gogou.gogou.model.RecoverPwdRequest;
import info.gogou.gogou.model.SignatureRequest;
import info.gogou.gogou.model.Subscriber;
import info.gogou.gogou.model.Trip;
import info.gogou.gogou.model.TripFilter;
import info.gogou.gogou.model.TripList;
import info.gogou.gogou.request.AddressRequest;
import info.gogou.gogou.request.DemandRequest;
import info.gogou.gogou.request.OAuth2LoginRequest;
import info.gogou.gogou.request.OrderRequest;
import info.gogou.gogou.request.PaymentRequest;
import info.gogou.gogou.request.SignRequest;
import info.gogou.gogou.request.SubscriberRequest;
import info.gogou.gogou.request.TripRequest;

//import com.netease.nim.uikit.NimUIKit;
//import com.netease.nim.uikit.cache.DataCacheManager;
//import com.netease.nimlib.sdk.NIMClient;
//import com.netease.nimlib.sdk.RequestCallback;
//import com.netease.nimlib.sdk.auth.AuthService;
//import com.netease.nimlib.sdk.auth.LoginInfo;

/**
 * Created by lxu on 2016-04-24.
 */
public class RESTRequestUtils {

    private static final String TAG = "RESTRequestUtils";

    public static void performGetRegCodeRequest(String username, String emailAddress, String languageCode, final RESTRequestListener<GenericResponse> listener) {

        SubscriberRequest.GetRegistrationCodeRequest request = new SubscriberRequest.GetRegistrationCodeRequest(username, emailAddress, languageCode);
        GogouApplication.getInstance().getSpiceManager().execute(request, new RequestListener<GenericResponse> () {

            @Override
            public void onRequestFailure(SpiceException e) {
                Log.e(TAG, "Error during request: " + e.getLocalizedMessage());
                if (listener != null)
                    listener.onGogouRESTRequestFailure();
            }

            @Override
            public void onRequestSuccess(GenericResponse response) {

                if (response.getErrorType() != ErrorType.NONE) {
                    Log.e(TAG, "Error during request: " + response.getMessage());
                } else {
                    Log.d(TAG, "Get registration code: " + response.getMessage());
                }
                if (listener != null)
                    listener.onGogouRESTRequestSuccess(response);
            }
        });
    }

    public static void performGetRecoverCodeRequest(String username, String emailAddress, String languageCode, final RESTRequestListener<GenericResponse> listener) {

        SubscriberRequest.GetRecoverCodeRequest request = new SubscriberRequest.GetRecoverCodeRequest(username, emailAddress, languageCode);
        GogouApplication.getInstance().getSpiceManager().execute(request, new RequestListener<GenericResponse> () {

            @Override
            public void onRequestFailure(SpiceException e) {
                Log.e(TAG, "Error during request: " + e.getLocalizedMessage());
                if (listener != null)
                    listener.onGogouRESTRequestFailure();
            }

            @Override
            public void onRequestSuccess(GenericResponse response) {

                if (response.getErrorType() != ErrorType.NONE) {
                    Log.e(TAG, "Error during request: " + response.getMessage());
                } else {
                    Log.d(TAG, "Get recover code: " + response.getMessage());
                }
                if (listener != null)
                    listener.onGogouRESTRequestSuccess(response);
            }
        });
    }

    public static void performRecoverPasswordRequest(RecoverPwdRequest recoverPwdRequest,
                                                     final RESTRequestListener<GenericResponse> listener) {

        SubscriberRequest.RecoverPasswordRequest request = new SubscriberRequest.RecoverPasswordRequest(recoverPwdRequest);
        GogouApplication.getInstance().getSpiceManager().execute(request, new RequestListener<GenericResponse> () {

            @Override
            public void onRequestFailure(SpiceException e) {
                Log.e(TAG, "Error during request: " + e.getLocalizedMessage());
                if (listener != null)
                    listener.onGogouRESTRequestFailure();
            }

            @Override
            public void onRequestSuccess(GenericResponse response) {

                if (response.getErrorType() != ErrorType.NONE) {
                    Log.e(TAG, "Error during request: " + response.getMessage());
                } else {
                    Log.d(TAG, "Recover password: " + response.getMessage());
                }
                if (listener != null)
                    listener.onGogouRESTRequestSuccess(response);
            }
        });
    }

    public static void performCreateSubscriberRequest(Subscriber subscriber, final RESTRequestListener<GenericResponse> listener) {

        SubscriberRequest.CreateSubscriberRequest request = new SubscriberRequest.CreateSubscriberRequest(subscriber);
        GogouApplication.getInstance().getSpiceManager().execute(request, new RequestListener<GenericResponse> () {

            @Override
            public void onRequestFailure(SpiceException e) {
                Log.e(TAG, "Error during request: " + e.getLocalizedMessage());
                if (listener != null)
                    listener.onGogouRESTRequestFailure();
            }

            @Override
            public void onRequestSuccess(GenericResponse response) {

                if (response.getErrorType() != ErrorType.NONE) {
                    Log.e(TAG, "Error during request: " + response.getMessage());
                } else {
                    Log.d(TAG, "Create subscriber: " + response.getMessage());
                }
                if (listener != null)
                    listener.onGogouRESTRequestSuccess(response);
            }
        });
    }

    public static void performUpdateSubscriberRequest(Subscriber subscriber, final RESTRequestListener<GenericResponse> listener) {

        SubscriberRequest.UpdateSubscriberRequest request = new SubscriberRequest.UpdateSubscriberRequest(subscriber);
        GogouApplication.getInstance().getSpiceManager().execute(request, new RequestListener<GenericResponse> () {

            @Override
            public void onRequestFailure(SpiceException e) {
                Log.e(TAG, "Error during request: " + e.getLocalizedMessage());
                if (listener != null)
                    listener.onGogouRESTRequestFailure();
            }

            @Override
            public void onRequestSuccess(GenericResponse response) {

                Log.d(TAG, "Update subscirber: " + response.getMessage());
                if (listener != null)
                    listener.onGogouRESTRequestSuccess(response);
            }
        });
    }

    public static void performOAuth2LoginRequest(String code, final RESTRequestListener<OAuth2Login> listener) {

        OAuth2LoginRequest.ThirdPartLoginRequest request = new OAuth2LoginRequest.ThirdPartLoginRequest(code);
        GogouApplication.getInstance().getSpiceManager().execute(request, new RequestListener<OAuth2Login>() {
            @Override
            public void onRequestFailure(SpiceException e) {
                Log.e(TAG, "Error during request: " + e.getLocalizedMessage());
                if (listener != null)
                    listener.onGogouRESTRequestFailure();
            }

            @Override
            public void onRequestSuccess(OAuth2Login response) {

                Log.d(TAG, "App server gets the respons.");
                if (listener != null)
                    listener.onGogouRESTRequestSuccess(response);
            }});
    }

    public static void performOauth2RefreshTokenRequest(String refreshToken, final RESTRequestListener<OAuth2Login> listener) {

        OAuth2LoginRequest.RefreshTokenRequest request = new OAuth2LoginRequest.RefreshTokenRequest(refreshToken);
        GogouApplication.getInstance().getSpiceManager().execute(request, new RequestListener<OAuth2Login>() {
            @Override
            public void onRequestFailure(SpiceException e) {
                Log.e(TAG, "Error during request: " + e.getLocalizedMessage());
                if (listener != null)
                    listener.onGogouRESTRequestFailure();
            }

            @Override
            public void onRequestSuccess(OAuth2Login response) {

                Log.d(TAG, "App server gets the respons.");
                if (listener != null)
                    listener.onGogouRESTRequestSuccess(response);
            }});
    }

    public static void performOAuth2CheckTokenRequest(String accessToken, final RESTRequestListener<GenericResponse> listener) {

        OAuth2LoginRequest.CheckAccessTokenRequest request = new OAuth2LoginRequest.CheckAccessTokenRequest(accessToken);
        GogouApplication.getInstance().getSpiceManager().execute(request, new RequestListener<GenericResponse>() {
            @Override
            public void onRequestFailure(SpiceException e) {
                Log.e(TAG, "Error during request: " + e.getLocalizedMessage());
                if (listener != null)
                    listener.onGogouRESTRequestFailure();
            }

            @Override
            public void onRequestSuccess(GenericResponse response) {

                Log.d(TAG, "App server gets the respons.");
                if (listener != null)
                    listener.onGogouRESTRequestSuccess(response);
            }});
    }

//    public static void performYunXinRequest(final RESTRequestListener listener) {
//
//        String userName = GogouApplication.getInstance().getUserName();
//        String encodedPassword = GogouApplication.getInstance().getPassword();
//        if (userName == null || encodedPassword == null)
//            return;
//        YunXinRequest.YunXinRegisterIDRequest request = new YunXinRequest.YunXinRegisterIDRequest(userName, encodedPassword);
//        GogouApplication.getInstance().getSpiceManager().execute(request, new RequestListener<Subscriber>() {
//            @Override
//            public void onRequestFailure(SpiceException e) {
//                Log.e(TAG, "Error during YunXin request: " + e.getLocalizedMessage());
//                if (listener != null)
//                    listener.onGogouRESTRequestFailure();
//            }
//
//            @Override
//            public void onRequestSuccess(final Subscriber response) {
//
//                if (response.getYunxinCode().equals("200")) {
//
//                    LoginInfo info = new LoginInfo(response.getId().toString(), response.getYunxinToken()); // config...
//                    RequestCallback<LoginInfo> callback =
//                            new RequestCallback<LoginInfo>() {
//                                @Override
//                                public void onSuccess(LoginInfo loginInfo) {
//                                    Log.d(TAG, "NM login successful: " + loginInfo.getAccount() + " and " + loginInfo.getToken());
//                                    NimUIKit.setAccount(response.getId().toString());
//                                    GogouApplication.getInstance().setLoggedOnNM(true);
//                                    // 构建缓存
//                                    DataCacheManager.buildDataCacheAsync();
//                                    if (listener != null)
//                                        listener.onGogouRESTRequestSuccess(loginInfo);
//                                }
//
//                                @Override
//                                public void onFailed(int i) {
//                                    Log.e(TAG, "NM Authentication service failed.");
//                                    if (listener != null)
//                                        listener.onGogouRESTRequestFailure();
//                                }
//
//                                @Override
//                                public void onException(Throwable throwable) {
//                                    Log.e(TAG, "NM Authentication service exception: " + throwable.getLocalizedMessage());
//                                    if (listener != null)
//                                        listener.onGogouRESTRequestFailure();
//                                }
//                            };
//                    NIMClient.getService(AuthService.class).login(info)
//                            .setCallback(callback);
//
//                } else {
//
//                    Log.e(TAG, "Error during YunXin request: " + response.getYunxinCode());
//                    if (listener != null)
//                        listener.onGogouRESTRequestFailure();
//                }
//            }
//        });
//    }

    public static void performDemandListRequest(DemandFilter demandFilter, final RESTRequestListener<DemandList> listener) {
        DemandRequest.GetDemandListRequest request = new DemandRequest.GetDemandListRequest(demandFilter);
        GogouApplication.getInstance().getSpiceManager().execute(request, new RequestListener<DemandList>() {
            @Override
            public void onRequestFailure(SpiceException e) {
                Log.e(TAG, "Error during demand list request: " + e.getLocalizedMessage());
                if (listener != null)
                    listener.onGogouRESTRequestFailure();
            }

            @Override
            public void onRequestSuccess(DemandList demandList) {

                if (demandList == null) {
                    Log.e(TAG, "Getting demand list failed!");
                    if (listener != null)
                        listener.onGogouRESTRequestFailure();
                } else {
                    Log.d(TAG, "There are " + demandList.size() + " demands");
                    if (listener != null)
                        listener.onGogouRESTRequestSuccess(demandList);
                }
            }
        });
    }

    public static void performTripListRequest(TripFilter tripFilter, final RESTRequestListener<TripList> listener) {
        TripRequest.GetTripListRequest request = new TripRequest.GetTripListRequest(tripFilter);
        GogouApplication.getInstance().getSpiceManager().execute(request, new RequestListener<TripList>() {
            @Override
            public void onRequestFailure(SpiceException e) {
                Log.e(TAG, "Error during trip list request: " + e.getLocalizedMessage());
                if (listener != null)
                    listener.onGogouRESTRequestFailure();
            }

            @Override
            public void onRequestSuccess(TripList tripList) {

                if (tripList == null) {
                    Log.e(TAG, "Getting trip list failed!");
                    if (listener != null)
                        listener.onGogouRESTRequestFailure();
                } else {
                    Log.d(TAG, "There are " + tripList.size() + " trips");
                    if (listener != null)
                        listener.onGogouRESTRequestSuccess(tripList);
                }
            }
        });
    }

    public static void performGetTripRequest(long tripId, final RESTRequestListener<Trip> listener) {
        TripRequest.GetTripRequest request = new TripRequest.GetTripRequest(tripId);
        GogouApplication.getInstance().getSpiceManager().execute(request, new RequestListener<Trip>() {
            @Override
            public void onRequestFailure(SpiceException e) {
                Log.e(TAG, "Error during trip get request: " + e.getLocalizedMessage());
                if (listener != null)
                    listener.onGogouRESTRequestFailure();
            }

            @Override
            public void onRequestSuccess(Trip trip) {

                if (trip == null) {
                    Log.e(TAG, "Getting trip failed!");
                    if (listener != null)
                        listener.onGogouRESTRequestFailure();
                } else {
                    Log.d(TAG, "Get trip id: " + trip.getId());
                    if (listener != null)
                        listener.onGogouRESTRequestSuccess(trip);
                }
            }
        });
    }

    public static void performOrderCreateRequest(Order order, final RESTRequestListener<GenericResponse> listener) {
        String userName = GogouApplication.getInstance().getUserName();
        String encodedPassword = GogouApplication.getInstance().getPassword();
        if (userName == null || encodedPassword == null)
            return;
        OrderRequest.CreateOrderRequest request = new OrderRequest.CreateOrderRequest(userName, encodedPassword, order);
        GogouApplication.getInstance().getSpiceManager().execute(request, new RequestListener<GenericResponse>() {

            @Override
            public void onRequestFailure(SpiceException spiceException) {
                Log.e(TAG, "Error during order create request: " + spiceException.getLocalizedMessage());
                if (listener != null)
                    listener.onGogouRESTRequestFailure();
            }

            @Override
            public void onRequestSuccess(GenericResponse genericResponse) {
                Log.d(TAG, "Order is created successfully.");
                if (listener != null)
                    listener.onGogouRESTRequestSuccess(genericResponse);
            }
        });
    }

    public static void performOrderUpdateRequest(Order order, final RESTRequestListener<GenericResponse> listener) {
        String userName = GogouApplication.getInstance().getUserName();
        String encodedPassword = GogouApplication.getInstance().getPassword();
        if (userName == null || encodedPassword == null)
            return;
        OrderRequest.UpdateOrderRequest request = new OrderRequest.UpdateOrderRequest(userName, encodedPassword, order);
        GogouApplication.getInstance().getSpiceManager().execute(request, new RequestListener<GenericResponse>() {

            @Override
            public void onRequestFailure(SpiceException spiceException) {
                Log.e(TAG, "Error during order update request: " + spiceException.getLocalizedMessage());
                if (listener != null)
                    listener.onGogouRESTRequestFailure();
            }

            @Override
            public void onRequestSuccess(GenericResponse genericResponse) {
                Log.d(TAG, "Order is updated successfully.");
                if (listener != null)
                    listener.onGogouRESTRequestSuccess(genericResponse);
            }
        });
    }

    public static void performOrderListRequest(OrderFilter orderFilter, final RESTRequestListener<OrderList> listener) {
        String userName = GogouApplication.getInstance().getUserName();
        String encodedPassword = GogouApplication.getInstance().getPassword();
        if (userName == null || encodedPassword == null)
            return;
        OrderRequest.GetOrderListRequest request = new OrderRequest.GetOrderListRequest(userName, encodedPassword, orderFilter);
        GogouApplication.getInstance().getSpiceManager().execute(request, new RequestListener<OrderList>() {
            @Override
            public void onRequestFailure(SpiceException e) {
                Log.e(TAG, "Error during order list request: " + e.getLocalizedMessage());
                if (listener != null)
                    listener.onGogouRESTRequestFailure();
            }

            @Override
            public void onRequestSuccess(OrderList orderList) {
                if (orderList == null) {
                    Log.e(TAG, "Getting order list null!");
                    if (listener != null)
                        listener.onGogouRESTRequestFailure();
                } else {
                    Log.d(TAG, "There are " + orderList.size() + " orders");
                    if (listener != null)
                        listener.onGogouRESTRequestSuccess(orderList);
                }
            }
        });
    }

    public static void performGetOrderRequest(long orderId, final RESTRequestListener<Order> listener) {
        String userName = GogouApplication.getInstance().getUserName();
        String encodedPassword = GogouApplication.getInstance().getPassword();
        if (userName == null || encodedPassword == null)
            return;
        OrderRequest.GetOrderRequest request = new OrderRequest.GetOrderRequest(userName, encodedPassword, orderId);
        GogouApplication.getInstance().getSpiceManager().execute(request, new RequestListener<Order>() {
            @Override
            public void onRequestFailure(SpiceException e) {
                Log.e(TAG, "Error during order get request: " + e.getLocalizedMessage());
                if (listener != null)
                    listener.onGogouRESTRequestFailure();
            }

            @Override
            public void onRequestSuccess(Order order) {
                if (order == null) {
                    Log.e(TAG, "Getting order failed!");
                    if (listener != null)
                        listener.onGogouRESTRequestFailure();
                } else {
                    Log.d(TAG, "Get Order id: " + order.getId());
                    if (listener != null)
                        listener.onGogouRESTRequestSuccess(order);
                }
            }
        });
    }

    public static void performAddressCreateRequest(Address address, final RESTRequestListener<GenericResponse> listener) {
        String userName = GogouApplication.getInstance().getUserName();
        String encodedPassword = GogouApplication.getInstance().getPassword();
        if (userName == null || encodedPassword == null)
            return;
        AddressRequest.CreateAddressRequest request = new AddressRequest.CreateAddressRequest(userName, encodedPassword, address);
        GogouApplication.getInstance().getSpiceManager().execute(request, new RequestListener<GenericResponse>() {

            @Override
            public void onRequestFailure(SpiceException spiceException) {
                Log.e(TAG, "Error during address create request: " + spiceException.getLocalizedMessage());
                if (listener != null)
                    listener.onGogouRESTRequestFailure();
            }

            @Override
            public void onRequestSuccess(GenericResponse genericResponse) {
                Log.d(TAG, "Address is added successfully.");
                if (listener != null)
                    listener.onGogouRESTRequestSuccess(genericResponse);
            }
        });
    }

    public static void performAddressListRequest(final RESTRequestListener<AddressList> listener) {
        String userName = GogouApplication.getInstance().getUserName();
        String encodedPassword = GogouApplication.getInstance().getPassword();
        if (userName == null || encodedPassword == null)
            return;
        AddressRequest.GetAddressListRequest request = new AddressRequest.GetAddressListRequest(userName, encodedPassword);
        GogouApplication.getInstance().getSpiceManager().execute(request, new RequestListener<AddressList>() {
            @Override
            public void onRequestFailure(SpiceException e) {
                Log.e(TAG, "Error during get address list request: " + e.getLocalizedMessage());
                if (listener != null)
                    listener.onGogouRESTRequestFailure();
            }

            @Override
            public void onRequestSuccess(AddressList addressList) {
                if (addressList == null) {
                    Log.e(TAG, "Getting address list failed!");
                    if (listener != null)
                        listener.onGogouRESTRequestFailure();
                } else {
                    Log.d(TAG, "There are " + addressList.size() + " address(es)");
                    if (listener != null)
                        listener.onGogouRESTRequestSuccess(addressList);
                }
            }
        });
    }

    public static void performPaymentMethodCreateRequest(PaymentMethod paymentMethod, final RESTRequestListener<GenericResponse> listener) {
        String userName = GogouApplication.getInstance().getUserName();
        String encodedPassword = GogouApplication.getInstance().getPassword();
        if (userName == null || encodedPassword == null)
            return;
        PaymentRequest.CreatePaymentMethodRequest request = new PaymentRequest.CreatePaymentMethodRequest(
                userName, encodedPassword, paymentMethod);
        GogouApplication.getInstance().getSpiceManager().execute(request, new RequestListener<GenericResponse>() {

            @Override
            public void onRequestFailure(SpiceException spiceException) {
                Log.e(TAG, "Error during payment method create request: " + spiceException.getLocalizedMessage());
                if (listener != null)
                    listener.onGogouRESTRequestFailure();
            }

            @Override
            public void onRequestSuccess(GenericResponse genericResponse) {
                Log.d(TAG, "Payment method is added successfully.");
                if (listener != null)
                    listener.onGogouRESTRequestSuccess(genericResponse);
            }
        });
    }

    public static void performPaymentMethodListRequest(final RESTRequestListener<PaymentMethodList> listener) {
        String userName = GogouApplication.getInstance().getUserName();
        String encodedPassword = GogouApplication.getInstance().getPassword();
        if (userName == null || encodedPassword == null)
            return;
        PaymentRequest.GetPaymentMethodListRequest request = new PaymentRequest.GetPaymentMethodListRequest(userName, encodedPassword);
        GogouApplication.getInstance().getSpiceManager().execute(request, new RequestListener<PaymentMethodList>() {
            @Override
            public void onRequestFailure(SpiceException e) {
                Log.e(TAG, "Error during get payment method list request: " + e.getLocalizedMessage());
                if (listener != null)
                    listener.onGogouRESTRequestFailure();
            }

            @Override
            public void onRequestSuccess(PaymentMethodList paymentList) {
                if (paymentList == null) {
                    Log.e(TAG, "Getting payment method list failed!");
                    if (listener != null)
                        listener.onGogouRESTRequestFailure();
                } else {
                    Log.d(TAG, "There are " + paymentList.size() + " payment method(s)");
                    if (listener != null)
                        listener.onGogouRESTRequestSuccess(paymentList);
                }
            }
        });
    }

    public static void performRSASignatureRequest(SignatureRequest signatureRequest, final RESTRequestListener<GenericResponse> listener) {

        SignRequest.CreateRSASignatureRequest request = new SignRequest.CreateRSASignatureRequest(signatureRequest);

        GogouApplication.getInstance().getSpiceManager().execute(request, new RequestListener<GenericResponse>() {

            @Override
            public void onRequestFailure(SpiceException spiceException) {
                Log.e(TAG, "Error during RSA signature create request: " + spiceException.getLocalizedMessage());
                if (listener != null)
                    listener.onGogouRESTRequestFailure();
            }

            @Override
            public void onRequestSuccess(GenericResponse genericResponse) {
                Log.d(TAG, "RSA signature request is successful.");
                if (listener != null)
                    listener.onGogouRESTRequestSuccess(genericResponse);
            }
        });
    }

    public static int handleRequestError(ErrorType retErrType) {

        if (retErrType == ErrorType.EER_NO_REG_CODE) {
            return R.string.notif_create_subscriber_no_reg_code;

        } else if (retErrType == ErrorType.EER_TIME_EXPIRED) {
            return R.string.notif_create_subscriber_time_expired;

        } else if (retErrType == ErrorType.EER_USER_EXISTS) {
            return R.string.notif_create_subscriber_user_exists;

        } else if (retErrType == ErrorType.EER_REG_CODE_SENT) {
            return R.string.notif_get_reg_code_duplicated;

        } else if (retErrType == ErrorType.EER_NO_USER_EXISTS) {
            return R.string.notif_get_recover_code_failed;

        } else if (retErrType == ErrorType.EER_NOT_MATCH) {
            return R.string.notif_create_subscriber_not_match;

        } else if (retErrType == ErrorType.ERR_EMAIL_FORMAT) {
            return R.string.notif_email_format_error;

        } else {
            return R.string.notif_unkown_failure;
        }
    }
}