/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/. */

package org.mozilla.fenix.ui.robots

import android.net.Uri
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.longClick
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.RootMatchers.isDialog
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withAlpha
import androidx.test.espresso.matcher.ViewMatchers.withContentDescription
import androidx.test.espresso.matcher.ViewMatchers.withEffectiveVisibility
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withParent
import androidx.test.espresso.matcher.ViewMatchers.withResourceName
import androidx.test.espresso.matcher.ViewMatchers.withText
import androidx.test.uiautomator.By
import androidx.test.uiautomator.Until
import org.hamcrest.Matchers
import org.hamcrest.Matchers.allOf
import org.mozilla.fenix.R
import org.mozilla.fenix.helpers.TestAssetHelper
import org.mozilla.fenix.helpers.click
import org.mozilla.fenix.share.ShareFragment

/**
 * Implementation of Robot Pattern for the history menu.
 */
class HistoryRobot {

    fun verifyHistoryMenuView() = assertHistoryMenuView()

    fun verifyEmptyHistoryView() = assertEmptyHistoryView()

    fun verifyVisitedTimeTitle() = assertVisitedTimeTitle()

    fun verifyFirstTestPageTitle(title: String) = assertTestPageTitle(title)

    fun verifyTestPageUrl(expectedUrl: Uri) = assertPageUrl(expectedUrl)

    fun verifyDeleteConfirmationMessage() = assertDeleteConfirmationMessage()

    fun verifyMultiSelectionCheckmark() = assertMultiSelectionCheckmark()

    fun verifyMultiSelectionCounter() = assertMultiSelectionCounter()

    fun verifyShareButton() = assertShareButton()

    fun verifyMultiSelectionOverflowMenu() = assertMultiSelectionOverflowMenu()

    fun verifyShareScrim() = assertShareScrim()

    fun verifyShareTabFavicon() = assertShareTabFavicon()

    fun verifyShareTabTitle() = assertShareTabTitle()

    fun verifyShareTabUrl() = assertShareTabUrl()

    fun openOverflowMenu() {
        overflowMenu().click()
    }

    fun clickThreeDotMenuDelete() {
        threeDotMenuDeleteButton().click()
    }

    fun clickDeleteHistoryButton() {
        deleteAllHistoryButton().click()
    }

    fun confirmDeleteAllHistory() {
        onView(withText("Delete"))
            .inRoot(isDialog())
            .check(matches(isDisplayed()))
            .click()
    }

    fun clickShareButton() {
        shareButton().click()
        mDevice.wait(Until.findObject(By.text("SHARE A LINK")), TestAssetHelper.waitingTime)
    }

    fun longTapSelectItem(url: Uri) {
        onView(withText(url.toString())).perform(longClick())
    }

    fun openMultiSelectionOverflowMenu() {
        multiSelectionOverflowMenu().click()
    }

    fun clickMultiSelectionDelete() {
        multiSelectionDeleteButton().click()
    }

    class Transition {
        fun goBack(interact: LibraryRobot.() -> Unit): LibraryRobot.Transition {
            goBackButton().click()

            LibraryRobot().interact()
            return LibraryRobot.Transition()
        }

        fun clickOpenNewTab(interact: HomeScreenRobot.() -> Unit): HomeScreenRobot.Transition {
            openInNewTabButton().click()

            HomeScreenRobot().interact()
            return HomeScreenRobot.Transition()
        }

        fun clickOpenPrivateTab(interact: HomeScreenRobot.() -> Unit): HomeScreenRobot.Transition {
            openInPrivateTabButton().click()

            HomeScreenRobot().interact()
            return HomeScreenRobot.Transition()
        }

        fun closeMenu(interact: HomeScreenRobot.() -> Unit): HomeScreenRobot.Transition {
            closeButton().click()

            HomeScreenRobot().interact()
            return HomeScreenRobot.Transition()
        }
    }
}

fun historyMenu(interact: HistoryRobot.() -> Unit): HistoryRobot.Transition {
    HistoryRobot().interact()
    return HistoryRobot.Transition()
}

private fun goBackButton() = onView(withContentDescription("Navigate up"))

private fun testPageTitle() = onView(allOf(withId(R.id.title), withText("Test_Page_1")))

private fun pageUrl() = onView(withId(R.id.url))

private fun overflowMenu() = onView(withId(R.id.overflow_menu))

private fun threeDotMenuDeleteButton() = onView(withId(R.id.simple_text))

private fun deleteAllHistoryButton() = onView(withId(R.id.delete_button))

private fun shareButton() = onView(withId(R.id.share_history_multi_select))

private fun multiSelectionOverflowMenu() =
    onView(
        allOf(
            withContentDescription("More options"),
            withEffectiveVisibility(ViewMatchers.Visibility.VISIBLE)
        )
    )

private fun openInNewTabButton() = onView(withText("Open in new tab"))

private fun openInPrivateTabButton() = onView(withText("Open in private tab"))

private fun multiSelectionDeleteButton() = onView(withText("Delete"))

private fun assertHistoryMenuView() {
    onView(
        allOf(withText("History"), withParent(withId(R.id.navigationToolbar)))
    )
        .check(matches(withEffectiveVisibility(ViewMatchers.Visibility.VISIBLE)))
}

private fun assertEmptyHistoryView() =
    onView(withId(R.id.history_empty_view)).check(matches(withText("No history here")))

private fun assertVisitedTimeTitle() =
    onView(withId(R.id.header_title)).check(matches(withText("Last 24 hours")))

private fun assertTestPageTitle(title: String) = testPageTitle()
    .check(matches(withEffectiveVisibility(ViewMatchers.Visibility.VISIBLE)))
    .check(matches(withText(title)))

private fun assertPageUrl(expectedUrl: Uri) = pageUrl()
    .check(matches(withEffectiveVisibility(ViewMatchers.Visibility.VISIBLE)))
    .check(matches(withText(Matchers.containsString(expectedUrl.toString()))))

private fun assertDeleteConfirmationMessage() =
    onView(withText("This will delete all of your browsing data."))
        .inRoot(isDialog())
        .check(matches(isDisplayed()))

private fun assertMultiSelectionCheckmark() =
    onView(withId(R.id.checkmark))
        .check(matches(isDisplayed()))

private fun assertMultiSelectionCounter() =
    onView(withText("1 selected")).check(matches(isDisplayed()))

private fun assertShareButton() =
    shareButton().check(matches(isDisplayed()))

private fun assertMultiSelectionOverflowMenu() =
    multiSelectionOverflowMenu().check(matches(withEffectiveVisibility(ViewMatchers.Visibility.VISIBLE)))

private fun assertShareScrim() =
    onView(withResourceName("closeSharingScrim"))
        .check(matches(withAlpha(ShareFragment.SHOW_PAGE_ALPHA)))

private fun assertShareTabTitle() =
    onView(withId(R.id.share_tab_title)).check(matches(isDisplayed()))

private fun assertShareTabFavicon() =
    onView(withId(R.id.share_tab_favicon)).check(matches(isDisplayed()))

private fun assertShareTabUrl() = onView(withId(R.id.share_tab_url))

private fun closeButton() = onView(withId(R.id.libraryClose))
