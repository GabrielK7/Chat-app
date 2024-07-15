package sk.demochat.chat_app.config;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;
import sk.demochat.chat_app.service.Message;
import sk.demochat.chat_app.service.MsgType;

import java.util.Objects;

@Component
@Slf4j
@RequiredArgsConstructor
public class WebSocketEventListener {
    private final SimpMessageSendingOperations messageOperations;
    @EventListener
    public void handleWebSocketDisconnectListener(SessionDisconnectEvent event) {
        StompHeaderAccessor headerAccessor = StompHeaderAccessor.wrap(event.getMessage());
        String username = (String) headerAccessor.getSessionAttributes().get("username");
        if (Objects.nonNull(username)) {
            log.info("User disconnected: {}", username);
            //Chat
            messageOperations.convertAndSend("/topic/chat", Message.builder().type(MsgType.LEAVE).sender(username).build());
        }
    }
}
