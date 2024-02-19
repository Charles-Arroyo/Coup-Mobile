package cs309.godclass.Logic;

import android.view.View;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import cs309.godclass.IView;
import cs309.godclass.Network.IServerRequest;
import cs309.godclass.Network.ServerRequest;
import cs309.godclass.RegistrationScreen;

public class RegistrationLogic implements IVolleyListener {

    IView r;
    IServerRequest serverRequest;

    public RegistrationLogic(IView r, IServerRequest serverRequest) {
        this.r = r;
        this.serverRequest = serverRequest;
        serverRequest.addVolleyListener(this);
    }

    public void registerUser(String name, String email, String password) throws JSONException {
        final String url = "http://10.0.2.2:8080/";
        JSONObject newUserObj = new JSONObject();
        newUserObj.put("name", name);
        newUserObj.put("email", email);
        newUserObj.put("password", password);

        serverRequest.sendToServer(url, newUserObj, "POST");

    }

    @Override
    public void onSuccess(String email) {
        if (email.length() > 0) {
            //startActivity(new Intent(getApplicationContext(), LoginScreen.class));
            r.showText("You are logged in!");
        } else {
            r.showText("Error with request, please try again");
        }
    }

    @Override
    public void onError (String errorMessage) {
        r.toastText(errorMessage);
        r.showText("Error with request, please try again");
    }
}
