package activity.newsfeed;

public final class Constants {

	public static final String GET_NEWSFEED_URL_PREFIX = "http://192.168.47.19:8080/news/getNewsList?num=";
	public static final String SEND_COMMENT_URL = "http://192.168.47.19:8080/news/addComment";
	public static final String[] IMAGES = new String[] {
			// Heavy images
			"http://cf6.thingd.com/default/287003521_57819ea5f53c.jpg",
			"http://cf3.fancyimgs.com/310/20130312/316531719014450763_411bace9d2dc.jpg",
			"http://cf3.fancyimgs.com/310/20130312/316410362960288849_39bd9cfd3fc1.jpg",
			"http://cf4.thingd.com/default/210411989657197535_c4caa2bb91f7.jpg",
			"http://cf4.fancyimgs.com/310/20130315/318556721058419757_720cdc5c3afe.jpg",
			"http://cf2.fancyimgs.com/310/20130314/317785317039938385_f84ef89f2b69.jpg",
			"http://cf6.thingd.com/default/294958463_f37f639ee0c8.jpeg",
			"http://cf2.fancyimgs.com/310/20130220/301842580746797833_8dd9bd5c7d68.jpg",
			"http://cf4.fancyimgs.com/310/20130211/295323486295955801_a382dfcba56b.jpg",
			"http://cf2.thingd.com/default/101180932102296741_c9857fc0f45b.jpg",
			"http://cf2.fancyimgs.com/310/20130317/319568132169535647_3c3280493f69.jpg",
			"http://cf2.fancyimgs.com/310/20130317/319572357997924605_0845507be389.png",
			"http://cf5.thingd.com/default/212281238948418989_ba2202c5eb68.jpg",
			"http://cf3.fancyimgs.com/310/20130313/317110066014587439_7be442003172.jpg",
			"http://cf1.fancyimgs.com/310/20130105/268599272453179389_1bd7337db78a.jpg",
			"http://cf3.thingd.com/default/263526271_56f15e9a7fe9.jpg",
			"http://cf3.fancyimgs.com/310/20130312/316536436641760917_9ae0ef6b38d7.jpg",
			"http://cf1.fancyimgs.com/310/20130303/309776449051560047_bd4114bae565.jpg",
			"http://cf6.thingd.com/default/287003521_57819ea5f53c.jpg",
			"http://cf3.fancyimgs.com/310/20130312/316531719014450763_411bace9d2dc.jpg",
			"http://cf3.fancyimgs.com/310/20130312/316410362960288849_39bd9cfd3fc1.jpg",
			"http://cf4.thingd.com/default/210411989657197535_c4caa2bb91f7.jpg",
			"http://cf4.fancyimgs.com/310/20130315/318556721058419757_720cdc5c3afe.jpg",
			"http://cf2.fancyimgs.com/310/20130314/317785317039938385_f84ef89f2b69.jpg",
			"http://cf6.thingd.com/default/294958463_f37f639ee0c8.jpeg",
			"http://cf2.fancyimgs.com/310/20130220/301842580746797833_8dd9bd5c7d68.jpg",
			"http://cf4.fancyimgs.com/310/20130211/295323486295955801_a382dfcba56b.jpg",
			"http://cf2.thingd.com/default/101180932102296741_c9857fc0f45b.jpg",
			"http://cf2.fancyimgs.com/310/20130317/319568132169535647_3c3280493f69.jpg",
			"http://cf2.fancyimgs.com/310/20130317/319572357997924605_0845507be389.png",
			"http://cf5.thingd.com/default/212281238948418989_ba2202c5eb68.jpg",
			"http://cf3.fancyimgs.com/310/20130313/317110066014587439_7be442003172.jpg",
			"http://cf1.fancyimgs.com/310/20130105/268599272453179389_1bd7337db78a.jpg",
			"http://cf3.thingd.com/default/263526271_56f15e9a7fe9.jpg",
			"http://cf3.fancyimgs.com/310/20130312/316536436641760917_9ae0ef6b38d7.jpg",
			"http://cf1.fancyimgs.com/310/20130303/309776449051560047_bd4114bae565.jpg",
			// Light images
			"http://tabletpcssource.com/wp-content/uploads/2011/05/android-logo.png",
			"http://simpozia.com/pages/images/stories/windows-icon.png",
			"https://si0.twimg.com/profile_images/1135218951/gmail_profile_icon3_normal.png",
			"http://www.krify.net/wp-content/uploads/2011/09/Macromedia_Flash_dock_icon.png",
			"http://radiotray.sourceforge.net/radio.png",
			"http://www.bandwidthblog.com/wp-content/uploads/2011/11/twitter-logo.png",
			"http://weloveicons.s3.amazonaws.com/icons/100907_itunes1.png",
			"http://weloveicons.s3.amazonaws.com/icons/100929_applications.png",
			"http://www.idyllicmusic.com/index_files/get_apple-iphone.png",
			"http://www.frenchrevolutionfood.com/wp-content/uploads/2009/04/Twitter-Bird.png",
			"http://3.bp.blogspot.com/-ka5MiRGJ_S4/TdD9OoF6bmI/AAAAAAAAE8k/7ydKtptUtSg/s1600/Google_Sky%2BMaps_Android.png",
			"http://www.desiredsoft.com/images/icon_webhosting.png",
			"http://goodereader.com/apps/wp-content/uploads/downloads/thumbnails/2012/01/hi-256-0-99dda8c730196ab93c67f0659d5b8489abdeb977.png",
			"http://1.bp.blogspot.com/-mlaJ4p_3rBU/TdD9OWxN8II/AAAAAAAAE8U/xyynWwr3_4Q/s1600/antivitus_free.png",
			"http://cdn3.iconfinder.com/data/icons/transformers/computer.png",
			"http://cdn.geekwire.com/wp-content/uploads/2011/04/firefox.png?7794fe",
			"https://ssl.gstatic.com/android/market/com.rovio.angrybirdsseasons/hi-256-9-347dae230614238a639d21508ae492302340b2ba",
			"http://androidblaze.com/wp-content/uploads/2011/12/tablet-pc-256x256.jpg",
			"http://www.theblaze.com/wp-content/uploads/2011/08/Apple.png",
			"http://1.bp.blogspot.com/-y-HQwQ4Kuu0/TdD9_iKIY7I/AAAAAAAAE88/3G4xiclDZD0/s1600/Twitter_Android.png",
			"http://3.bp.blogspot.com/-nAf4IMJGpc8/TdD9OGNUHHI/AAAAAAAAE8E/VM9yU_lIgZ4/s1600/Adobe%2BReader_Android.png",
			"http://cdn.geekwire.com/wp-content/uploads/2011/05/oovoo-android.png?7794fe",
			"http://icons.iconarchive.com/icons/kocco/ndroid/128/android-market-2-icon.png",
			"http://thecustomizewindows.com/wp-content/uploads/2011/11/Nicest-Android-Live-Wallpapers.png",
			"http://c.wrzuta.pl/wm16596/a32f1a47002ab3a949afeb4f",
			"http://macprovid.vo.llnwd.net/o43/hub/media/1090/6882/01_headline_Muse.jpg",
			// Special cases
			"assets://Living Things @#&=+-_.,!()~'%20.jpg", // Image from assets
			"http://upload.wikimedia.org/wikipedia/ru/b/b6/Как_кот_с_мышами_воевал.png", // Link with UTF-8
			"https://www.eff.org/sites/default/files/chrome150_0.jpg", // Image from HTTPS
			"http://bit.ly/soBiXr", // Redirect link
			"", // Empty link
			"http://wrong.site.com/corruptedLink", // Wrong link
	};

	private Constants() {
	}

	public static class Config {
		public static final boolean DEVELOPER_MODE = false;
	}
	
	public static class Extra {
		public static final String IMAGES = "com.nostra13.example.universalimageloader.IMAGES";
		public static final String IMAGE_POSITION = "com.nostra13.example.universalimageloader.IMAGE_POSITION";
	}
}
