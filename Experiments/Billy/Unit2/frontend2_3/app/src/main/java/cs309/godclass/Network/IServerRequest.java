package cs309.godclass.Network;

import org.json.JSONObject;

import cs309.godclass.Logic.IVolleyListener;

public interface IServerRequest {
    public void sendToServer(String url, JSONObject newUserObj, String methodType);
    public void addVolleyListener(IVolleyListener logic);
}
