package io.verticon.dmescripts.model;

import java.util.List;
import java.util.ArrayList;

import javax.persistence.PostPersist;
import javax.persistence.PostRemove;
import javax.persistence.PostUpdate;
import javax.persistence.PrePersist;
import javax.persistence.PreRemove;
import javax.persistence.PreUpdate;

public class ProductListener {

	private static List<ProductListenerSubscriber> subscribers = new ArrayList<ProductListenerSubscriber>();
	public static void addSubscriber(ProductListenerSubscriber subscriber) { subscribers.add(subscriber); }
	
    @PrePersist
 	public void methodInvokedBeforePersist(Product product) {
 		report(product, "Before Persist");
 	}

 	@PostPersist
 	public void methodInvokedAfterPersist(Product product) {
 		report(product, "After Persist");
 		subscribers.forEach(subscriber -> subscriber.inserted(product) );
 	}

 	@PreUpdate
 	public void methodInvokedBeforeUpdate(Product product) {
 		report(product, "Before Update");
 	}

 	@PostUpdate
 	public void methodInvokedAfterUpdate(Product product) {
 		report(product, "After Update");
 		subscribers.forEach(subscriber -> subscriber.updated(product) );
 	}

 	@PreRemove
 	private void methodInvokedBeforeRemove(Product product) {
 		report(product, "Before Remove");
 	}

 	@PostRemove
 	public void methodInvokedAfterRemove(Product product) {
 		report(product, "After Remove");
 		subscribers.forEach(subscriber -> subscriber.removed(product) );
 	}

 	private void report(Product product, String action) {
 		//System.out.printf("%d %s: product = %s\n", System.identityHashCode(this), action, product.getName());
 	}
}
