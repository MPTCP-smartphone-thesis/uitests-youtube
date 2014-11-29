package youtube;

import utils.Utils;

import com.android.uiautomator.core.UiObject;
import com.android.uiautomator.core.UiObjectNotFoundException;
import com.android.uiautomator.core.UiScrollable;
import com.android.uiautomator.core.UiSelector;
import com.android.uiautomator.testrunner.UiAutomatorTestCase;

public class LaunchSettings extends UiAutomatorTestCase {

	private static final String ID_YOUT_LIST = "com.google.android.youtube:id/results";
	private static final String ID_YOUT_SEARCH = "com.google.android.youtube:id/menu_search";
	private static final String ID_YOUT_TEXT = "android:id/search_src_text";
	private static final String ID_YOUT_PLAYER = "com.google.android.youtube:id/player_fragment";
	private static final String ID_INIT_OK = "com.google.android.youtube:id/ok";

	private static final int MAX_VIDEOS = 2;
	private static final int TIME_VIDEO = 30000;

	private void goToTheChannel() {
		sleep(1500);
		UiObject search = Utils.getObjectWithId(ID_YOUT_SEARCH);
		while (!search.exists()) {
			getUiDevice().pressBack();
			sleep(1500);
			search = Utils.getObjectWithId(ID_YOUT_SEARCH);
		}

		assertTrue("Cannot do search", Utils.click(ID_YOUT_SEARCH));

		sleep(1000);

		Utils.setText(Utils.getObjectWithId(ID_YOUT_TEXT),
				"Stanford openflow channel");

		sleep(1000);

		getUiDevice().pressEnter();

		sleep(1000);

		Utils.click(new UiObject(new UiSelector().resourceId(ID_YOUT_LIST)
				.childSelector(
						new UiSelector()
								.className("android.widget.FrameLayout")
								.instance(0))));
	}

	private void seeVideos() {
		Utils.click(Utils.getObjectWithText("Videos"));

		sleep(1000);

		UiScrollable list = Utils.getScrollableWithId(ID_YOUT_LIST);
		boolean canScroll = true;
		int nVid = 0;

		// Goes on until no more video
		for (int k = 0; k < 3 && nVid < MAX_VIDEOS && canScroll; k++) {
			// Take the list to count the number of items in it
			int nb_childs = Utils.getChildCount(list);

			sleep(1500);

			// -4 to avoid the fuzzy behavior with the video player fragment
			for (int i = 0; i < 2 * nb_childs - 4 && nVid < MAX_VIDEOS; i = i + 2) {
				// 'Got it' button that we can have sometimes
				if (Utils.hasObject(ID_INIT_OK)) {
					Utils.click(ID_INIT_OK);
					sleep(1000);
				}

				UiObject video = new UiObject(new UiSelector().resourceId(
						ID_YOUT_LIST).childSelector(
						new UiSelector()
								.className("android.widget.FrameLayout")
								.instance(i)));
				assertTrue("Reference a non-existing video", Utils.click(video));
				sleep(TIME_VIDEO);
				nVid++;
				// Got it button that we can have sometimes
				if (Utils.hasObject(ID_INIT_OK)) {
					Utils.click(ID_INIT_OK);
					sleep(1000);
				}

				getUiDevice().pressBack();
			}
			sleep(1000);
			UiObject player = Utils.getObjectWithId(ID_YOUT_PLAYER);
			Utils.swipeLeft(player, 100);
			canScroll = Utils.scrollForward(list);
		}
	}

	public void testDemo() throws UiObjectNotFoundException {
		assertTrue("OOOOOpps",
				Utils.openApp(this, "YouTube", "com.google.android.youtube"));

		// initial menu: just clic on OK
		sleep(1000);
		if (Utils.hasObject(ID_INIT_OK))
			Utils.click(ID_INIT_OK);
		goToTheChannel();
		seeVideos();
	}

}
