package eu.silvenia.shipballot.creature;

import com.badlogic.gdx.ai.fsm.State;
import com.badlogic.gdx.ai.msg.Telegram;

/**
 * Created by Johnnie Ho on 4-7-2015.
 */
public enum PlayerState implements State<Player> {

    GROUNDED() {
        @Override
        public void update(Player playerAgent) {
            if(playerAgent.canJump())
                playerAgent.move();
            else{
                playerAgent.stateMachine.changeState(PlayerState.AIRBORNE);
            }
        }
    },

    AIRBORNE() {
        @Override
        public void update(Player playerAgent) {
            if(playerAgent.canJump())
                playerAgent.stateMachine.changeState(PlayerState.GROUNDED);
        }
    };

    @Override
    public void enter(Player playerAgent) {

    }

    @Override
    public void exit(Player playerAgent) {}

    @Override
    public boolean onMessage(Player playerAgent, Telegram telegram) {
        // ignore telegrams for now
        return false;
    }
}