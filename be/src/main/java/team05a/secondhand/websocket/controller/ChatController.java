package team05a.secondhand.websocket.controller;

import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import team05a.secondhand.member.resolver.MemberId;
import team05a.secondhand.redis.service.RedisPublisher;
import team05a.secondhand.websocket.chat_room.data.dto.ChatMessageRequest;
import team05a.secondhand.websocket.chat_room.data.dto.ChatRoomCreateRequest;
import team05a.secondhand.websocket.chat_room.data.dto.ChatRoomCreateResponse;
import team05a.secondhand.websocket.service.ChatService;

@Slf4j
@RequiredArgsConstructor
@RestController
public class ChatController {

	private final RedisPublisher redisPublisher;
	private final ChatService chatService;

	@MessageMapping("/chats/messages/{chatRoomId}")
	@SendTo("")
	public void message(@DestinationVariable Long chatRoomId, ChatMessageRequest chatMessageRequest) {
		log.info("WebSocket Username : {}", chatMessageRequest.getSenderId());
		// 채팅방에 메세지 전송
		redisPublisher.publish(ChannelTopic.of("chatRoom" + chatRoomId), chatMessageRequest);
		log.info("레디스 서버에 메세지 전송 완료");
		chatService.saveMessage(chatRoomId, chatMessageRequest);
	}

	@PostMapping("/api/chat/room")
	public ResponseEntity<ChatRoomCreateResponse> createChatRoom(@MemberId Long memberId,
		@RequestBody ChatRoomCreateRequest chatRoomCreateRequest) {
		ChatRoomCreateResponse chatRoomCreateResponse = chatService.createChatRoom(memberId, chatRoomCreateRequest);
		return ResponseEntity.status(HttpStatus.CREATED)
			.body(chatRoomCreateResponse);
	}
}
