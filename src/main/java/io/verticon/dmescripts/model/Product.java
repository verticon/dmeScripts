package io.verticon.dmescripts.model;

import java.util.List;
import java.util.logging.Level;

import javax.persistence.*;

import org.apache.commons.logging.LogFactory;

import com.gargoylesoftware.htmlunit.NicelyResynchronizingAjaxController;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.DomNode;
import com.gargoylesoftware.htmlunit.html.DomNodeList;
import com.gargoylesoftware.htmlunit.html.HtmlAnchor;
import com.gargoylesoftware.htmlunit.html.HtmlImage;
import com.gargoylesoftware.htmlunit.html.HtmlPage;

import io.verticon.dmescripts.Factory;

@Entity
@Table(name="Products")
//@EntityListeners(ProductListener.class)
public class Product {
	private static Long idCounter = 1L;

	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String type;
    private String category;
    private String insurance;
    private String imageUrl;

	@OneToMany(mappedBy = "product")
	private List<Order> orders;

	public Product() {}

	public Product(String name, String type, String category, String insurance, String imageUrl) {
		id = idCounter++;
		this.name = name;
		this.type = type;
		this.category = category;
		this.insurance = insurance;
		this.imageUrl = imageUrl;
	}
	

	public Long getId() {
    	return id;
    }
    
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getInsurance() {
        return insurance;
    }

    public void setInsurance(String insurance) {
        this.insurance = insurance;
    }

    public List<Order> getOrders() {
    	return orders;
    }

    public void setOrders(List<Order> orders) {
    	this.orders = orders;
    }

    @Override
    public boolean equals(Object that) {
        if (that == null) { return false; }
        
        if (this == that) { return true; }

        if (!Product.class.isAssignableFrom(that.getClass())) { return false; }

        final Product thatProduct = (Product) that;
        return thatProduct.getId() == this.getId();
    }

    @Override
    public String toString() {
        return String.format("%s: Name=%s Insurance=%s", Product.class.getSimpleName(), name, insurance);
    }

    public static class Load {

    	private DataAccessService dataService = Factory.sDataService;

    	private void loadCategories(String type, HtmlPage page) throws Exception {

        	System.out.printf("\n%s:\n", type);

        	String className = "easycatalogimg";
        	String selector = String.format(".%s", className);
        	DomNode theCategories = page.querySelector(selector);
        	if (theCategories == null) throw new Exception("Cannot get the product categories container"); 

        	className = "product-image";
        	selector = String.format(".%s", className);
        	DomNodeList<DomNode> theAnchors = theCategories.querySelectorAll(selector);
        	if (theAnchors == null) throw new Exception("Cannot get the list of product categories"); 
        	for (DomNode theNode : theAnchors) {
            	loadCategory(type, (HtmlAnchor) theNode);
        	}
    	}

        private void loadCategory(String type, HtmlAnchor anchor) throws Exception {

        	String category = anchor.getAttribute("title");
        	System.out.printf("\n\t%s:\n", category);

        	HtmlPage page = anchor.click();
        	if (page == null) throw new Exception(String.format("Cannot get the %s products page", category)); 

        	String className = "category-products";
        	String selector = String.format(".%s", className);
        	DomNode theProducts = page.querySelector(selector);
        	if (theProducts == null) throw new Exception(String.format("Cannot get the %s products container", category)); 
    		
        	className = "main-info";
        	selector = String.format(".%s", className);
        	DomNodeList<DomNode> theProductList = theProducts.querySelectorAll(selector);
        	if (theProductList == null) throw new Exception(String.format("Cannot get the %s products list", category)); 

        	for (DomNode theProduct : theProductList) {

            	className = "product-name";
            	selector = String.format(".%s", className);
            	DomNode theProductNameNode = theProduct.querySelector(selector);
            	HtmlAnchor theProductNameAnchor = (HtmlAnchor) theProductNameNode.getFirstChild();
            	String theProductName = theProductNameAnchor.getTextContent();

            	className = "prolabel-wrapper";
            	selector = String.format(".%s", className);
            	DomNode theProductImageNode = theProduct.querySelector(selector);
            	HtmlImage theProductImage = (HtmlImage) theProductImageNode.getFirstByXPath("a/img");
            	String theProductImageUrl = theProductImage.getAttribute("src");

            	dataService.addProduct(new Product(theProductName, type, category, Factory.getRandomInsurance(), theProductImageUrl));

            	System.out.printf("\t\t%s\n", theProductName);
        	}
        }

        private void loadProductData() {

        	LogFactory.getFactory().setAttribute("org.apache.commons.logging.Log", "org.apache.commons.logging.impl.NoOpLog");
        	java.util.logging.Logger.getLogger("com.gargoylesoftware.htmlunit.DefaultCssErrorHandler").setLevel(Level.SEVERE); 

        	try (final WebClient webClient = new WebClient()) {

        		webClient.setAjaxController(new NicelyResynchronizingAjaxController());
        		webClient.getOptions().setCssEnabled(false);

        		String url = "https://aeroflowinc.com/shop/catheters?limit=36";
            	HtmlPage page = webClient.getPage(url);

            	loadCategories("Catheters", page);
            }
            catch (Exception ex) {
            	System.err.printf("An exception occurred while loading product data:\n%s\n", ex);
            }
        }

        public static void main(String[] args) {
        	System.out.println("Loading Product Data ...");
        	new Load().loadProductData();
        	System.out.println("\nProduct Data Loaded");
        }
    }
}
