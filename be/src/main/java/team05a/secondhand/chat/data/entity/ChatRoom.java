package team05a.secondhand.chat.data.entity;

import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import team05a.secondhand.member.data.entity.Member;
import team05a.secondhand.product.data.entity.Product;

@Entity
@NoArgsConstructor
@Getter
public class ChatRoom {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "chat_room_id")
	private Long id;
	private String uuid;
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "product_id")
	private Product product;
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "buyer_id")
	private Member buyer;
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "seller_id")
	private Member seller;
	private Long sellerLastReadMessageId;
	private Long buyerLastReadMessageId;

	@Builder
	private ChatRoom(Product product, Member buyer, Member seller, Long sellerLastReadMessageId,
		Long buyerLastReadMessageId) {
		this.uuid = UUID.randomUUID().toString();
		this.product = product;
		this.buyer = buyer;
		this.seller = seller;
		this.sellerLastReadMessageId = sellerLastReadMessageId;
		this.buyerLastReadMessageId = buyerLastReadMessageId;
	}

	public void deleteSeller() {
		this.seller = null;
	}

	public void deleteBuyer() {
		this.buyer = null;
	}
}
