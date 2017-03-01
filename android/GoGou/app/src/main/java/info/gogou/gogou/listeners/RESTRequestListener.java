package info.gogou.gogou.listeners;

/**
 * Created by lxu on 2016-04-24.
 */
public interface RESTRequestListener<RESULT> {

    void onGogouRESTRequestFailure();

    void onGogouRESTRequestSuccess(RESULT result);
}
