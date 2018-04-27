package io.verticon.dmescripts.model;

public interface ProductListenerSubscriber {
	void inserted(Product product);
	void updated(Product product);
	void removed(Product product);
}
