package com.adobe.apphub;

public class AppListItem {

	public String type;
	public String name;
	public String productUrl;
	public String imageUrl;
	public String description;
	public String lastUpdated;
	public String inAppPurchase;
	public String rating;
	
	public AppListItem(String name,String type,String productUrl,String imageUrl,String description,String lastUpdated
			,String inAppPurchase,String rating) {
		
		this.name = name;
		this.type = type;
		this.productUrl = productUrl;
		this.imageUrl = imageUrl;
		this.description = description;
		this.lastUpdated = lastUpdated;
		this.inAppPurchase = inAppPurchase;
		this.rating = rating;
	}

	public String getType() {
		return type;
	}

	public String getName() {
		return name;
	}

	public String getProductUrl() {
		return productUrl;
	}

	public String getImageUrl() {
		return imageUrl;
	}

	public String getDescription() {
		return description;
	}

	public String getLastUpdated() {
		return lastUpdated;
	}

	public String getInAppPurchase() {
		return inAppPurchase;
	}

	public String getRating() {
		return rating;
	}

	@Override
	public String toString() {
		return name;
	}

	
}
