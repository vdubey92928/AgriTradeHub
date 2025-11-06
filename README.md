# AgriTradeHub

├── .gitattributes
├── .gitignore
├── .mvn
    └── wrapper
    │   └── maven-wrapper.properties
├── Public
    ├── ProductImage
    │   ├── 2cfdb3a1-ab80-4983-86a6-2abfb0ca4b0d_orange.png
    │   ├── 30ed8a2c-a104-416b-a292-cfdb65a462fc_orange.png
    │   ├── 39a80f5c-df24-4f81-80f9-bef1859698d0_orange.png
    │   ├── 4f1ac109-a190-471d-93bf-6543f04043b7_banana.webp
    │   ├── 70af8ccd-fba2-40d7-89ba-7db4b138e447_istockphoto-153737841-612x612.jpg
    │   ├── 731300dc-f5f1-48e0-bf07-76dca09357df_wheat.jpg
    │   ├── 93921484-fbe1-4924-99b8-191ece219259_orange.png
    │   ├── b551cf99-7f57-4df6-8708-24e29f599d80_istockphoto-153737841-612x612.jpg
    │   ├── bcb1ccc4-8fef-4666-948e-88d8ece631ab_orange.png
    │   ├── cd95face-c5af-4c56-90b1-b21edd351249_mango.webp
    │   └── f7c3e9e3-1afb-4237-8bce-4e50b2e01f46_orange.png
    └── ProfilePicture
    │   ├── 1755929737534_kali-linux-on-mobile.jpg
    │   ├── 1756547003551_40.jpg
    │   └── 1756634803899_37.jpg
├── README.md
├── mvnw
├── mvnw.cmd
├── pom.xml
└── src
    ├── main
        ├── java
        │   └── com
        │   │   └── myproject
        │   │       ├── Agritrade
        │   │           └── config
        │   │           │   └── GeminiConfig.java
        │   │       └── AgritradeHub
        │   │           ├── API
        │   │               ├── PaymentService.java
        │   │               └── SendAutoEmail.java
        │   │           ├── AgritradeHubApplication.java
        │   │           ├── Controller
        │   │               ├── AdminController.java
        │   │               ├── FarmerController.java
        │   │               ├── GeminiController.java
        │   │               ├── MainController.java
        │   │               └── MerchantController.java
        │   │           ├── Model
        │   │               ├── AllUsers.java
        │   │               ├── Category.java
        │   │               ├── Enquiry.java
        │   │               ├── Orders.java
        │   │               ├── Payment.java
        │   │               └── Products.java
        │   │           ├── Repository
        │   │               ├── AllUsersRepository.java
        │   │               ├── CategoryRepository.java
        │   │               ├── EnquiryRepository.java
        │   │               ├── OrdersRepository.java
        │   │               ├── PaymentRepository.java
        │   │               └── ProductsRepository.java
        │   │           └── service
        │   │               └── GeminiService.java
        └── resources
        │   ├── application.properties
        │   ├── static
        │       ├── assets
        │       │   ├── css
        │       │   │   ├── bootstrap.min.css
        │       │   │   ├── demo.css
        │       │   │   ├── ready.css
        │       │   │   ├── ready.css.map
        │       │   │   └── ready.min.css
        │       │   ├── fonts
        │       │   │   ├── line-awesome.eot
        │       │   │   ├── line-awesome.svg
        │       │   │   ├── line-awesome.ttf
        │       │   │   ├── line-awesome.woff
        │       │   │   └── line-awesome.woff2
        │       │   ├── img
        │       │   │   ├── menu.png
        │       │   │   ├── menu2.png
        │       │   │   ├── profile.jpg
        │       │   │   ├── profile2.jpg
        │       │   │   └── user_img.jpg
        │       │   ├── js
        │       │   │   ├── core
        │       │   │   │   ├── bootstrap.min.js
        │       │   │   │   ├── jquery.3.2.1.min.js
        │       │   │   │   └── popper.min.js
        │       │   │   ├── demo.js
        │       │   │   ├── plugin
        │       │   │   │   ├── bootstrap-notify
        │       │   │   │   │   └── bootstrap-notify.min.js
        │       │   │   │   ├── bootstrap-toggle
        │       │   │   │   │   └── bootstrap-toggle.min.js
        │       │   │   │   ├── chart-circle
        │       │   │   │   │   └── circles.min.js
        │       │   │   │   ├── chartist
        │       │   │   │   │   ├── chartist.min.js
        │       │   │   │   │   └── plugin
        │       │   │   │   │   │   └── chartist-plugin-tooltip.min.js
        │       │   │   │   ├── jquery-mapael
        │       │   │   │   │   ├── jquery.mapael.min.js
        │       │   │   │   │   └── maps
        │       │   │   │   │   │   ├── README.txt
        │       │   │   │   │   │   ├── france_departments.js
        │       │   │   │   │   │   ├── france_departments.min.js
        │       │   │   │   │   │   ├── usa_states.js
        │       │   │   │   │   │   ├── usa_states.min.js
        │       │   │   │   │   │   ├── world_countries.js
        │       │   │   │   │   │   ├── world_countries.min.js
        │       │   │   │   │   │   ├── world_countries_mercator.js
        │       │   │   │   │   │   ├── world_countries_mercator.min.js
        │       │   │   │   │   │   ├── world_countries_miller.js
        │       │   │   │   │   │   └── world_countries_miller.min.js
        │       │   │   │   ├── jquery-scrollbar
        │       │   │   │   │   └── jquery.scrollbar.min.js
        │       │   │   │   └── jquery-ui-1.12.1.custom
        │       │   │   │   │   └── jquery-ui.min.js
        │       │   │   ├── ready.js
        │       │   │   └── ready.min.js
        │       │   └── sass
        │       │   │   ├── ready.scss
        │       │   │   └── ready
        │       │   │       ├── _background.scss
        │       │   │       ├── _components.scss
        │       │   │       ├── _layouts.scss
        │       │   │       ├── _line-awesome.scss
        │       │   │       ├── _outlinefocus.scss
        │       │   │       ├── _plugins.scss
        │       │   │       ├── _responsive.scss
        │       │   │       ├── _typography.scss
        │       │   │       ├── _variables.scss
        │       │   │       ├── components
        │       │   │           ├── _alerts.scss
        │       │   │           ├── _badges.scss
        │       │   │           ├── _buttons.scss
        │       │   │           ├── _cards.scss
        │       │   │           ├── _charts.scss
        │       │   │           ├── _dropdowns.scss
        │       │   │           ├── _inputs.scss
        │       │   │           ├── _maps.scss
        │       │   │           ├── _modals.scss
        │       │   │           ├── _navbars.scss
        │       │   │           ├── _navpills.scss
        │       │   │           ├── _navsearch.scss
        │       │   │           ├── _paginations.scss
        │       │   │           ├── _sliders.scss
        │       │   │           └── _tables.scss
        │       │   │       └── plugins
        │       │   │           ├── _chartist.scss
        │       │   │           ├── _jqueryscrollbar.scss
        │       │   │           ├── _jqueryui.scss
        │       │   │           └── _toggle.scss
        │       └── images
        │       │   ├── bg_img.jpg
        │       │   └── fav-icon.jpg
        │   └── templates
        │       ├── Admin
        │           ├── AddCategory.html
        │           ├── Adminbase.html
        │           ├── ChangePassword.html
        │           ├── Dashboard.html
        │           ├── Enquiry.html
        │           ├── ManageFarmers.html
        │           ├── ManageMerchants.html
        │           ├── ViewOrder.html
        │           └── ViewProfile.html
        │       ├── ContactUs.html
        │       ├── Farmer
        │           ├── AddProduct.html
        │           ├── ChangePassword.html
        │           ├── Dashbaord.html
        │           ├── Farmerbase.html
        │           ├── ManageOrders.html
        │           ├── ManageProduct.html
        │           ├── UpdateProduct.html
        │           └── UserProfile.html
        │       ├── FarmerRegistration.html
        │       ├── Login.html
        │       ├── Merchant
        │           ├── BuyProduct.html
        │           ├── ChangePassword.html
        │           ├── Dashboard.html
        │           ├── Merchantbase.html
        │           ├── MyOrders.html
        │           └── UserProfile.html
        │       ├── MerchantRegistration.html
        │       ├── aboutus.html
        │       ├── base.html
        │       ├── gemini.html
        │       ├── index.html
        │       └── services.html
    └── test
        └── java
            └── com
                └── myproject
                    └── AgritradeHub
                        └── AgritradeHubApplicationTests.java


/.gitattributes:
