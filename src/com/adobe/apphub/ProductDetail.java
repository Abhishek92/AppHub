package com.adobe.apphub;

import android.app.Activity;
import android.os.Bundle;

public class ProductDetail extends Activity {
	private Bundle bundle;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_product_detail);
		
		bundle = getIntent().getBundleExtra("detail_bundle");
		ProductDetailFragment fragment = new ProductDetailFragment();
		fragment.setArguments(bundle);
		getFragmentManager().beginTransaction().add(R.id.container, fragment).commit();
	}
}
