package gr.ntua.ece.softeng18b

import gr.ntua.ece.softeng18b.client.RestAPI
import gr.ntua.ece.softeng18b.client.model.PriceInfo
import gr.ntua.ece.softeng18b.client.model.PriceInfoList
import gr.ntua.ece.softeng18b.client.model.Product
import gr.ntua.ece.softeng18b.client.model.Shop
import gr.ntua.ece.softeng18b.client.rest.RestCallFormat
import java.lang.reflect.Array
import spock.lang.Shared
import spock.lang.Specification
import spock.lang.Stepwise

import groovy.json.JsonSlurper

@Stepwise class RestAPISpecification extends Specification {

	private static final String IGNORED = System.setProperty("IGNORE_SSL_ERRORS", "true")

    private static final String HOST = System.getProperty("gretty.host")
    private static final String PORT = System.getProperty("gretty.httpsPort")

	private static String id;
	private static String id_shop;
	private static Product post_pr;
	private static Shop post_sh;

	private static String[] test_products_id = new Array[3];
	private static String[] test_shops_id = new Array[3];


   @Shared RestAPI api = new RestAPI(HOST, PORT as Integer, true)

    def "User logins"() {
        when:
        api.login("user", "user", RestCallFormat.JSON)

        then:
        api.isLoggedIn()

    }

    def "User adds product" (){
        when:
        Product sent = new Product(
            name       : "Product",
            description: "Description",
            category   : "Category",
            tags       : ["x", "y", "z"],
            withdrawn  : false
        )
        Product returned = api.postProduct(sent, RestCallFormat.JSON)
		id = returned.id;
		post_pr = sent;

        then:
        returned.name == sent.name &&
        returned.description == sent.description &&
        returned.category == sent.category &&
        returned.tags == sent.tags &&
		returned.withdrawn == sent.withdrawn
    }

	def "User adds product_test_1" (){
		when:
		Product sent = new Product(
			name: "Προϊόν 1",
			description: "Περιγραφή Προϊόντος 1",
			category: "Πρώτη Κατηγορία Προϊόντος",
			tags: ["Υπολογιστές"]
		)
		Product returned = api.postProduct(sent, RestCallFormat.JSON)
		test_products_id[0] = returned.id;

		then:
		returned.name == sent.name &&
		returned.description == sent.description &&
		returned.category == sent.category &&
		returned.tags == sent.tags
	}

	def "User adds product_test_2" (){
		when:
		Product sent = new Product(
			name: "Product 2",
			description: "Product Description 2",
			category: "Second Product Category",
			tags: ["Music"]
		)
		Product returned = api.postProduct(sent, RestCallFormat.JSON)
		test_products_id[1] = returned.id;

		then:
		returned.name == sent.name &&
		returned.description == sent.description &&
		returned.category == sent.category &&
		returned.tags == sent.tags
	}

	def "User adds product_test_3" (){
		when:
		Product sent = new Product(
			name: "Product 3",
			description: "Product Description 3",
			category: "First Product Category",
			tags: ["Music", "Entertainment"]
		)
		Product returned = api.postProduct(sent, RestCallFormat.JSON)
		test_products_id[2] = returned.id;

		then:
		returned.name == sent.name &&
		returned.description == sent.description &&
		returned.category == sent.category &&
		returned.tags == sent.tags
	}

	def "User gets product" (){
		when:
		Product returned = api.getProduct(id, RestCallFormat.JSON)

		then:
		returned.name == post_pr.name &&
		returned.description == post_pr.description &&
		returned.category == post_pr.category &&
		returned.tags == post_pr.tags &&
		returned.withdrawn == post_pr.withdrawn
	}

	def "User updates (puts) product" (){
		when:
		Product sent = new Product(
			name       : "ProductX",
			description: "DescriptionX",
			category   : "CategoryX",
			tags       : ["xx", "yyy", "zz"],
			withdrawn  : false
		)
		Product returned = api.putProduct(id, sent, RestCallFormat.JSON)
		//System.out.println(id);

		then:
		returned.id.equals(id);
		returned.name == sent.name &&
		returned.description == sent.description &&
		returned.category == sent.category &&
		returned.tags == sent.tags &&
		returned.withdrawn == sent.withdrawn
	}

	def "User patches product" (){
		when:
		Product returned0 = api.patchProduct(id, "name", "Patched nameXXXZZZ", RestCallFormat.JSON)

		then:
		returned0.name.equals("Patched nameXXXZZZ")

		when:
		Product returned = api.patchProduct(id, "description", "Patched dscr", RestCallFormat.JSON)

		then:
		returned.description.equals("Patched dscr");

		when:
		Product returned2 = api.patchProduct(id, "status", "WITHDRAWN", RestCallFormat.JSON)

		then:
		returned2.withdrawn

		when:
		Product returned3 = api.patchProduct(id, "status", "ACTIVE", RestCallFormat.JSON)

		then:
		!returned3.withdrawn

		when:
		Product returned4 = api.patchProduct(id, "tags", "pt1,pt2,pt3,pt4", RestCallFormat.JSON)

		then:
		returned4.tags == ["pt1", "pt2", "pt3", "pt4"]
	}

	def "User deletes product" (){
		when:
		System.out.println("Deleting product with ID: "+this.id);
		api.deleteProduct(id, RestCallFormat.JSON)
		Product returned = api.getProduct(id, RestCallFormat.JSON)

		then:
		returned.withdrawn == true
	}

	def "User gets list of products" (){
		when:
		List<Product> returned = api.getProducts(5, 5, "ACTIVE", "id|ASC", RestCallFormat.JSON).products

		then:
		for(int i=0; i<returned.size(); i++) {
			if(i<returned.size()-1) returned.get(i).id<returned.get(i+1).id;
			returned.get(i).withdrawn = false;
		}
		if(returned.size() > 0) Integer.parseInt(returned.get(0).id)>=5;
		returned.size() <= 5;

		when:
		List<Product> returned2 = api.getProducts(10, 2, "ACTIVE", "id|ASC", RestCallFormat.JSON).products

		then:
		for(int i=0; i<returned2.size(); i++) {
			if(i<returned2.size()-1) returned2.get(i).id<returned2.get(i+1).id;
			returned2.get(i).withdrawn = false;
		}
		if(returned2.size() > 0) Integer.parseInt(returned2.get(0).id)>=10;
		returned2.size() <= 2;

		when:
		List<Product> returned3 = api.getProducts(0, 5, "WITHDRAWN", "id|DESC", RestCallFormat.JSON).products

		then:
		for(int i=0; i<returned3.size(); i++) {
			if(i<returned3.size()-1) returned3.get(i).id>returned3.get(i+1).id;
			returned3.get(i).withdrawn = true;
		}
		if(returned3.size() > 0) Integer.parseInt(returned3.get(0).id)>=0;
		returned3.size() <= 5;

		when:
		List<Product> returned4 = api.getProducts(0, 50, "ACTIVE", "name|DESC", RestCallFormat.JSON).products

		then:
		for(int i=0; i<returned4.size(); i++) {
			if(i<returned4.size()-1) returned4.get(i).name > returned4.get(i+1).name;
			returned4.get(i).withdrawn = false;
		}
		if(returned4.size() > 0) Integer.parseInt(returned4.get(0).id)>=0;
		returned4.size() <= 50;

	}

	def "User adds shop" (){
		when:
		Shop sent = new Shop(
			name       	: "Dummy Shop",
			address	   	: "Addr of dummy shop",
			lat   		: 23.0432,
			lng			: 27.0234,
			tags       	: ["x", "y", "z"],
			withdrawn  	: false
		)
		Shop returned = api.postShop(sent, RestCallFormat.JSON)
		id_shop = returned.id;
		post_sh = sent;
		//System.out.println(id);

		then:
		returned.name == sent.name &&
		returned.address == sent.address &&
		returned.lat == sent.lat &&
		returned.lng == sent.lng &&
		returned.tags == sent.tags &&
		returned.withdrawn == sent.withdrawn
	}

	def "User adds shop__test_1" (){
		when:
		Shop sent = new Shop(
			name 	:  "Shop 1",
			address :  "Address 1",
			lat	 	: 38.455561,
			lng 	: 23.595554,
			tags	: ["Music", "Computers"]
		)
		Shop returned = api.postShop(sent, RestCallFormat.JSON)
		test_shops_id[0] = returned.id;

		then:
		returned.name == sent.name &&
		returned.address == sent.address &&
		returned.lat == sent.lat &&
		returned.lng == sent.lng &&
		returned.tags == sent.tags
	}

	def "User adds shop_test_2" (){
		when:
		Shop sent = new Shop(
			name 	:  "Shop 2",
			address :  "Address 2",
			lat		: 39.01324,
			lng		: 23.77223,
			tags	: ["Music", "Computers", "Books"]
		)
		Shop returned = api.postShop(sent, RestCallFormat.JSON)
		test_shops_id[1] = returned.id;

		then:
		returned.name == sent.name &&
		returned.address == sent.address &&
		returned.lat == sent.lat &&
		returned.lng == sent.lng &&
		returned.tags == sent.tags
	}

	def "User adds shop_test_3" (){
		when:
		Shop sent = new Shop(
			name 	:  "Shop 3",
			address :  "Address 3",
			lat		: 38.01667,
			lng		: 24.83333,
			tags	: ["Phones", "Computers", "Books"]
		)
		Shop returned = api.postShop(sent, RestCallFormat.JSON)
		test_shops_id[2] = returned.id;

		then:
		returned.name == sent.name &&
		returned.address == sent.address &&
		returned.lat == sent.lat &&
		returned.lng == sent.lng &&
		returned.tags == sent.tags
	}


	def "User gets shop" (){
		when:
		Shop returned = api.getShop(id_shop, RestCallFormat.JSON)
		//System.out.println(id);

		then:
		returned.name == post_sh.name &&
		returned.address == post_sh.address &&
		returned.lat == post_sh.lat &&
		returned.lng == post_sh.lng &&
		returned.tags == post_sh.tags &&
		returned.withdrawn == post_sh.withdrawn
	}

	def "User updates (puts) shop" (){
		when:
		Shop sent = new Shop(
			name       	: "Dummy ShopXXX",
			address	   	: "Addr of dummy shopXXX",
			lat   		: 24.0432,
			lng			: 25.0234,
			tags       	: ["xx", "yx", "zzz"],
			withdrawn  	: false
		)
		Shop returned = api.putShop(id_shop, sent, RestCallFormat.JSON)
		//System.out.println(id);

		then:
		returned.id.equals(id_shop);
		returned.name == sent.name &&
		returned.address == sent.address &&
		returned.lat == sent.lat &&
		returned.lng == sent.lng &&
		returned.tags == sent.tags &&
		returned.withdrawn == sent.withdrawn
	}

	def "User patches shop" (){
		when:
		Shop returned = api.patchShop(id_shop, "name", "Patched name", RestCallFormat.JSON)

		then:
		returned.name.equals("Patched name");

		when:
		Shop returned01 = api.patchShop(id_shop, "address", "Patched address", RestCallFormat.JSON)

		then:
		returned01.address.equals("Patched address");

		when:
		Shop returned02 = api.patchShop(id_shop, "lat", "26.0432", RestCallFormat.JSON)

		then:
		returned02.lat == 26.0432;

		when:
		Shop returned2 = api.patchShop(id_shop, "status", "WITHDRAWN", RestCallFormat.JSON)

		then:
		returned2.withdrawn

		when:
		Shop returned3 = api.patchShop(id_shop, "status", "ACTIVE", RestCallFormat.JSON)

		then:
		!returned3.withdrawn

		when:
		Shop returned4 = api.patchShop(id_shop, "tags", "pt1,pt2,pt3,pt4", RestCallFormat.JSON);

		then:
		returned4.tags == ["pt1", "pt2", "pt3", "pt4"]
	}

	def "User deletes shop" (){
		when:
		System.out.println("Deleting (as user) shop with ID: "+this.id_shop);
		api.deleteShop(id_shop, RestCallFormat.JSON)
		Shop returned = api.getShop(id_shop, RestCallFormat.JSON)

		then:
		returned.withdrawn == true
	}

	def "User adds price"() {
		when:
		double price = 66.99
		String dateFrom = "2019-03-28"
		String dateTo = "2019-03-29"
		String shopId = id_shop
		String productId = id
		PriceInfoList list = api.postPrice(price, dateFrom, dateTo, productId, shopId, RestCallFormat.JSON)

		then:
		list.total == 2 &&
		list.prices.every { PriceInfo p ->
			p.price == price &&
			p.productId == productId
			p.shopId == shopId

		} &&
		list.prices[0].date == dateFrom &&
		list.prices[1].date == dateTo

	}

	def "User adds  and gets prices"() {
		when:
		def jsonSlurper = new JsonSlurper()
		Object it = jsonSlurper.parseText('{"price": 10.00, "dateFrom":  "2019-02-23", "dateTo":  "2019-02-24", "shopIndex": 0, "productIndex": 0}')
		//println it
		double price = it.price
		String dateFrom = it.dateFrom
		String dateTo = it.dateTo
		String shopId = test_shops_id[it.shopIndex]
		String productId = test_products_id[it.productIndex]
		PriceInfoList list = api.postPrice(price, dateFrom, dateTo, productId, shopId, RestCallFormat.JSON)

		it = jsonSlurper.parseText('{"price": 11.20, "dateFrom":  "2019-02-23", "dateTo":  "2019-02-24", "shopIndex": 1, "productIndex": 0}')
		//println it
		price = it.price
		dateFrom = it.dateFrom
		dateTo = it.dateTo
		shopId = test_shops_id[it.shopIndex]
		productId = test_products_id[it.productIndex]
		list = api.postPrice(price, dateFrom, dateTo, productId, shopId, RestCallFormat.JSON)

		it = jsonSlurper.parseText('{"price": 10.54, "dateFrom":  "2019-02-23", "dateTo":  "2019-02-24", "shopIndex": 2, "productIndex": 0}')
		//println it
		price = it.price
		dateFrom = it.dateFrom
		dateTo = it.dateTo
		shopId = test_shops_id[it.shopIndex]
		productId = test_products_id[it.productIndex]
		list = api.postPrice(price, dateFrom, dateTo, productId, shopId, RestCallFormat.JSON)

		it = jsonSlurper.parseText('{"price": 32.99, "dateFrom":  "2019-02-23", "dateTo":  "2019-02-24", "shopIndex": 0, "productIndex": 1}')
		//println it
		price = it.price
		dateFrom = it.dateFrom
		dateTo = it.dateTo
		shopId = test_shops_id[it.shopIndex]
		productId = test_products_id[it.productIndex]
		list = api.postPrice(price, dateFrom, dateTo, productId, shopId, RestCallFormat.JSON)

		it = jsonSlurper.parseText('{"price": 36.99, "dateFrom":  "2019-02-23", "dateTo":  "2019-02-24", "shopIndex": 1, "productIndex": 1}')
		//println it
		price = it.price
		dateFrom = it.dateFrom
		dateTo = it.dateTo
		shopId = test_shops_id[it.shopIndex]
		productId = test_products_id[it.productIndex]
		list = api.postPrice(price, dateFrom, dateTo, productId, shopId, RestCallFormat.JSON)

		it = jsonSlurper.parseText('{"price": 37.99, "dateFrom":  "2019-02-23", "dateTo":  "2019-02-24", "shopIndex": 2, "productIndex": 1}')
		//println it
		price = it.price
		dateFrom = it.dateFrom
		dateTo = it.dateTo
		shopId = test_shops_id[it.shopIndex]
		productId = test_products_id[it.productIndex]
		list = api.postPrice(price, dateFrom, dateTo, productId, shopId, RestCallFormat.JSON)

		it = jsonSlurper.parseText('{"price": 97.30, "dateFrom":  "2019-02-23", "dateTo":  "2019-02-24", "shopIndex": 0, "productIndex": 2}')
		//println it
		price = it.price
		dateFrom = it.dateFrom
		dateTo = it.dateTo
		shopId = test_shops_id[it.shopIndex]
		productId = test_products_id[it.productIndex]
		list = api.postPrice(price, dateFrom, dateTo, productId, shopId, RestCallFormat.JSON)

		it = jsonSlurper.parseText('{"price": 92.90, "dateFrom":  "2019-02-23", "dateTo":  "2019-02-24", "shopIndex": 1, "productIndex": 2}')
		//println it
		price = it.price
		dateFrom = it.dateFrom
		dateTo = it.dateTo
		shopId = test_shops_id[it.shopIndex]
		productId = test_products_id[it.productIndex]
		list = api.postPrice(price, dateFrom, dateTo, productId, shopId, RestCallFormat.JSON)

		it = jsonSlurper.parseText('{"price": 90.00, "dateFrom":  "2019-02-23", "dateTo":  "2019-02-24", "shopIndex": 2, "productIndex": 2}')
		//println it
		price = it.price
		dateFrom = it.dateFrom
		dateTo = it.dateTo
		shopId = test_shops_id[it.shopIndex]
		productId = test_products_id[it.productIndex]
		list = api.postPrice(price, dateFrom, dateTo, productId, shopId, RestCallFormat.JSON)


		list = api.getPrices(0,10,null,null,null,"2019-02-23","2019-02-23",[test_shops_id[0], test_shops_id[1], test_shops_id[2]],[test_products_id[0]],null,["price|ASC"],RestCallFormat.JSON)
		//lst = jsonSlurper.parseText('{ "List":[{"price": 10.00, "date":  "2019-02-23", "shopIndex": 0, "productIndex":  0},{"price": 10.54, "date":  "2019-02-23", "shopIndex": 2, "productIndex":  0},{"price": 11.20, "date":  "2019-02-23", "shopIndex": 1, "productIndex":  0}] }')
		List<PriceInfo> pil = list.prices;

		then:

		list.getTotal()  == 3 //&&
		for(PriceInfo pi : pil) {
		pi.date == "2019-02-23"
		pi.productId == test_products_id[0]
		(pi.shopId == test_shops_id[0] || pi.shopId == test_shops_id[1] || pi.shopId == test_shops_id[2])
		}
		/*list.prices.every { PriceInfo p ->
			p.price == price &&
			p.productId == productId
			p.shopId == shopId

		} &&
		list.prices[0].date == dateFrom &&
		list.prices[1].date == dateTo*/

	}

	def "User gets list of shops" (){
		when:
		List<Shop> returned = api.getShops(5, 5, "ACTIVE", "id|ASC", RestCallFormat.JSON).shops

		then:
		for(int i=0; i<returned.size(); i++) {
			if(i<returned.size()-1) returned.get(i).id<returned.get(i+1).id;
			returned.get(i).withdrawn = false;
		}
		if(returned.size() > 0) Integer.parseInt(returned.get(0).id)>=5;
		returned.size() <= 5;

		when:
		List<Shop> returned2 = api.getShops(10, 2, "ACTIVE", "id|ASC", RestCallFormat.JSON).shops

		then:
		for(int i=0; i<returned2.size(); i++) {
			if(i<returned2.size()-1) returned2.get(i).id<returned2.get(i+1).id;
			returned2.get(i).withdrawn = false;
		}
		if(returned2.size() > 0) Integer.parseInt(returned2.get(0).id)>=10;
		returned2.size() <= 2;

		when:
		List<Shop> returned3 = api.getShops(0, 5, "WITHDRAWN", "id|DESC", RestCallFormat.JSON).shops

		then:
		for(int i=0; i<returned3.size(); i++) {
			if(i<returned3.size()-1) returned3.get(i).id>returned3.get(i+1).id;
			returned3.get(i).withdrawn = true;
		}
		if(returned.size() > 0) Integer.parseInt(returned3.get(0).id)>=0;
		returned3.size() <= 5;

		when:
		List<Shop> returned4 = api.getShops(0, 50, "ACTIVE", "name|DESC", RestCallFormat.JSON).shops

		then:
		for(int i=0; i<returned4.size(); i++) {
			if(i<returned4.size()-1) returned4.get(i).name > returned4.get(i+1).name;
			returned4.get(i).withdrawn = false;
		}
		if(returned4.size() > 0) Integer.parseInt(returned4.get(0).id)>=0;
		returned4.size() <= 50;

	}

	def "Admin logins"() {
		when:
		api.login("admin", "admin", RestCallFormat.JSON)
		System.out.println("Logging in as admin");

		then:
		api.isLoggedIn()

	}

	def "Admin deletes product" (){
		when:
		boolean flag = false;
		System.out.println("Deleting product with ID: "+this.id);
		api.deleteProduct(id, RestCallFormat.JSON)
		try {
			Product returned = api.getProduct(id, RestCallFormat.JSON)
		}
		catch(RuntimeException e) {
			if(e.message.equals("Error 404: Not Found")) flag = true;
		}
		then:
		flag
	}

	def "Admin deletes product_test_1" (){
		when:
		boolean flag = false;
		System.out.println("Deleting product with ID: "+test_products_id[0]);
		api.deleteProduct(test_products_id[0], RestCallFormat.JSON)
		try {
			Product returned = api.getProduct(test_products_id[0], RestCallFormat.JSON)
		}
		catch(RuntimeException e) {
			if(e.message.equals("Error 404: Not Found")) flag = true;
		}
		then:
		flag
	}

	def "Admin deletes product_test_2" (){
		when:
		boolean flag = false;
		System.out.println("Deleting product with ID: "+test_products_id[1]);
		api.deleteProduct(test_products_id[1], RestCallFormat.JSON)
		try {
			Product returned = api.getProduct(test_products_id[1], RestCallFormat.JSON)
		}
		catch(RuntimeException e) {
			if(e.message.equals("Error 404: Not Found")) flag = true;
		}
		then:
		flag
	}

	def "Admin deletes product_test_3" (){
		when:
		boolean flag = false;
		System.out.println("Deleting product with ID: "+test_products_id[2]);
		api.deleteProduct(test_products_id[2], RestCallFormat.JSON)
		try {
			Product returned = api.getProduct(test_products_id[2], RestCallFormat.JSON)
		}
		catch(RuntimeException e) {
			if(e.message.equals("Error 404: Not Found")) flag = true;
		}
		then:
		flag
	}


	def "Admin deletes shop" (){
		when:
		boolean flag = false;
		System.out.println("Deleting shop with ID: "+this.id_shop);
		api.deleteShop(id_shop, RestCallFormat.JSON)
		try {
			Shop returned = api.getShop(id_shop, RestCallFormat.JSON)
		}
		catch(RuntimeException e) {
			if(e.message.equals("Error 404: Not Found")) flag = true;
		}
		then:
		flag
	}

	def "Admin deletes shop_test_1" (){
		when:
		boolean flag = false;
		System.out.println("Deleting shop with ID: "+this.test_shops_id[0]);
		api.deleteShop(test_shops_id[0], RestCallFormat.JSON)
		try {
			Shop returned = api.getShop(test_shops_id[0], RestCallFormat.JSON)
		}
		catch(RuntimeException e) {
			if(e.message.equals("Error 404: Not Found")) flag = true;
		}
		then:
		flag
	}

	def "Admin deletes shop_test_2" (){
		when:
		boolean flag = false;
		System.out.println("Deleting shop with ID: "+this.test_shops_id[1]);
		api.deleteShop(test_shops_id[1], RestCallFormat.JSON)
		try {
			Shop returned = api.getShop(test_shops_id[1], RestCallFormat.JSON)
		}
		catch(RuntimeException e) {
			if(e.message.equals("Error 404: Not Found")) flag = true;
		}
		then:
		flag
	}

	def "Admin deletes shop_test_3" (){
		when:
		boolean flag = false;
		System.out.println("Deleting shop with ID: "+this.test_shops_id[2]);
		api.deleteShop(test_shops_id[2], RestCallFormat.JSON)
		try {
			Shop returned = api.getShop(test_shops_id[2], RestCallFormat.JSON)
		}
		catch(RuntimeException e) {
			if(e.message.equals("Error 404: Not Found")) flag = true;
		}
		then:
		flag
	}

}
