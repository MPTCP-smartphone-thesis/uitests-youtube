package youtube;

import utils.Utils;

import com.android.uiautomator.core.UiObject;
import com.android.uiautomator.core.UiObjectNotFoundException;
import com.android.uiautomator.core.UiScrollable;
import com.android.uiautomator.core.UiSelector;
import com.android.uiautomator.testrunner.UiAutomatorTestCase;

public class LaunchSettings extends UiAutomatorTestCase {

	private static String ID_YOUT_LIST = "com.google.android.youtube:id/results";
	private static String ID_YOUT_TITLE = "android:id/action_bar_title";
	private static String ID_YOUT_SEARCH = "com.google.android.youtube:id/menu_search";
	private static String ID_YOUT_TEXT = "android:id/search_src_text";
	private static String ID_YOUT_PLAYER = "com.google.android.youtube:id/player_fragment";

	private void seeAllPopular() {
		sleep(1500);
		// Return to main menu  if needed
		while (!Utils.hasText(ID_YOUT_TITLE, "What to Watch")) {
			getUiDevice().pressBack();
			sleep(1500);
		}
		
		Utils.click(ID_YOUT_SEARCH);

		sleep(1000);
		
		Utils.setText(
Utils.getObjectWithId(ID_YOUT_TEXT),
				"Stanford openflow channel");

		sleep(1000);

		getUiDevice().pressEnter();

		sleep(1000);
		
		Utils.click(new UiObject(
				new UiSelector().resourceId(ID_YOUT_LIST).childSelector(
						new UiSelector()
						.className("android.widget.FrameLayout")
								.instance(0))));

		//
		// Utils.click(Utils.getObjectWithDescription("What to Watch, Open guide"));
		//
		// sleep(1000);
		//
		// Utils.click(Utils.getObjectWithClassName(
		// "android.widget.RelativeLayout", 8));
		//
		// sleep(1000);

		// Click on first button (Popular on YouTube)
		// UiObject popular = new UiObject(new UiSelector().resourceId(
		// ID_YOUT_LIST).childSelector(
		// new UiSelector().className("android.widget.FrameLayout")
		// .instance(0)));
		//
		// UiScrollable mainList = Utils.getScrollableWithId(ID_YOUT_LIST);
		//
		// while (!Utils.click(popular)) {
		// Utils.scrollForward(mainList);
		// sleep(1000);
		// popular = new UiObject(new UiSelector().resourceId(ID_YOUT_LIST)
		// .childSelector(
		// new UiSelector().className(
		// "android.widget.FrameLayout").instance(0)));
		// }
		//
		// sleep(1000);

		// Click on Most Popular Right Now
		// UiObject most_popular = Utils.getObjectWithClassName(
		// "android.widget.FrameLayout", 0);
		// assertTrue("Button most popular right now is not here",
		// Utils.click(most_popular));

		Utils.click(Utils.getObjectWithText("Videos"));

		sleep(1000);

		UiScrollable list = Utils.getScrollableWithId(ID_YOUT_LIST);
		boolean canScroll = true;
		
		// Goes on until no more video
		for (int k = 0; k < 3 && canScroll; k++) {
			// Take the list to count the number of items in it
			int nb_childs = Utils.getChildCount(list);

			sleep(1500);

			// -4 to avoid the fuzzy behavior with the video player fragment
			for (int i = 0; i < 2 * nb_childs - 4; i = i + 2) {
				UiObject video = new UiObject(new UiSelector().resourceId(
						ID_YOUT_LIST).childSelector(
						new UiSelector()
								.className("android.widget.FrameLayout")
								.instance(i)));
				assertTrue("Reference a non-existing video", Utils.click(video));
				sleep(3000);
				getUiDevice().pressBack();
			}
			UiObject player = Utils.getObjectWithId(ID_YOUT_PLAYER);
			assertTrue("Failed to swipe the video player fragment",
					Utils.swipeLeft(player, 100));
			canScroll = Utils.scrollForward(list);
		}
	}


	public void testDemo() throws UiObjectNotFoundException {
		assertTrue("OOOOOpps",
				Utils.openApp(this, "YouTube", "com.google.android.youtube"));
		sleep(10000);

		Utils.launchTcpdump("youtube", 900);
		seeAllPopular();
		Utils.killTcpdump();
	}

}