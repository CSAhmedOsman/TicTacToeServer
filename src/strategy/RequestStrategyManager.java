/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package strategy;

import java.util.HashMap;
import java.util.Map;
import util.Constants;

/**
 *
 * @author w
 */
public class RequestStrategyManager {

    private final Map<Integer, RequestHandlerStrategy> strategies = new HashMap<>();

    private void setRequestStrategy(int key, RequestHandlerStrategy requestStrategy) {
        strategies.put(key, requestStrategy);
    }

    public RequestHandlerStrategy getRequestStrategy(int key) {
        return strategies.get(key);
    }

    public void populateStrategies() {
        setRequestStrategy(Constants.REGISTER, new RegisterStrategy());
        setRequestStrategy(Constants.LOGIN, new LoginStrategy());
        setRequestStrategy(Constants.GET_AVAILIABLE_PLAYERS, new GetAvailableStrategy());
        setRequestStrategy(Constants.SEND_MESSAGE, new SendMessageStrategy());
        setRequestStrategy(Constants.BROADCAST_MESSAGE, new SendBroadcastStrategy());
        setRequestStrategy(Constants.GET_DATA_OF_PLAYER, new GetDataStrategy());
        setRequestStrategy(Constants.UPDATE_USER_PROFILE, new UpdateProfileStrategy());
        setRequestStrategy(Constants.SEND_INVITE, new SendInviteStrategy());
        setRequestStrategy(Constants.ACCEPT_GAME, new AcceptGameStrategy());
        setRequestStrategy(Constants.SEND_MOVE, new SendMoveStrategy());
        setRequestStrategy(Constants.UPDATE_SCORE, new UpdateScoreStrategy());
        setRequestStrategy(Constants.EXIT_GAME, new ExitGameStrategy());
        setRequestStrategy(Constants.ADD_FRIEND, new AddFriendStrategy());
        setRequestStrategy(Constants.REMOVE_FRIEND, new RemoveFriendStrategy());
        setRequestStrategy(Constants.BLOCK_PLAYER, new BlockPlayerStrategy());
        setRequestStrategy(Constants.UN_BLOCK_PLAYER, new UnblockPlayerStrategy());
        setRequestStrategy(Constants.ONLINE, new MakeOnlineStrategy());
    }
}
