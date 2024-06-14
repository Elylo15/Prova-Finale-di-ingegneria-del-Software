package it.polimi.ingsw.messages.waitingForPlayerState;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class NewHostMessageTest {

    private newHostMessage message;
    private String newHostNickname;

    @BeforeEach
    void setup() {
        newHostNickname = "Test Host";
        message = new newHostMessage(newHostNickname);
    }

    @Test
    @DisplayName("New host nickname is correctly retrieved")
    void newHostNicknameIsCorrectlyRetrieved() {
        Assertions.assertEquals(newHostNickname, message.getNewHostNickname());
    }

    @Test
    @DisplayName("New host nickname is correctly updated")
    void newHostNicknameIsCorrectlyUpdated() {
        String updatedNickname = "Updated Host";
        message.setNewHostNickname(updatedNickname);
        Assertions.assertEquals(updatedNickname, message.getNewHostNickname());
    }
}