package org.eddard.grpc.client;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class RpcController {

    @Autowired
    private GRPCClient grpcClient;

    @RequestMapping(value = "/rpc", method = RequestMethod.GET)
    @ResponseBody
    public String rpc() {
        try {
            grpcClient.sendHeartBeat("Heartbeat");
        } catch (Exception ex) {
            return "Failed";
        }

        return "Succeeded";
    }
}
