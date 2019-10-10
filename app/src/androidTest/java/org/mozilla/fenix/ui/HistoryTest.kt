/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/. */

package org.mozilla.fenix.ui

import okhttp3.mockwebserver.MockWebServer
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mozilla.fenix.helpers.AndroidAssetDispatcher
import org.mozilla.fenix.helpers.HomeActivityTestRule
import org.mozilla.fenix.helpers.TestAssetHelper
import org.mozilla.fenix.ui.robots.homeScreen
import org.mozilla.fenix.ui.robots.navigationToolbar

/**
 *  Tests for verifying basic functionality of history
 *
 */
class HistoryTest {
    /* ktlint-disable no-blank-line-before-rbrace */ // This imposes unreadable grouping.
    private lateinit var mockWebServer: MockWebServer

    @get:Rule
    val activityTestRule = HomeActivityTestRule()

    @Before
    fun setUp() {
        mockWebServer = MockWebServer().apply {
            setDispatcher(AndroidAssetDispatcher())
            start()
        }
    }

    @After
    fun tearDown() {
        mockWebServer.shutdown()
        // Clearing all history data after each test to avoid overlapping data
    }

    @Test
    fun noHistoryItemsInCacheTest() {
        homeScreen {
        }.openThreeDotMenu {
            verifyLibraryButton()
        }.openLibrary {
            verifyHistoryButton()
        }.openHistory {
            verifyHistoryMenuView()
            verifyEmptyHistoryView()
        }
    }

    @Test
    fun visitedUrlHistoryTest() {
        val firstWebPage = TestAssetHelper.getGenericAsset(mockWebServer, 1)

        navigationToolbar {
        }.enterURLAndEnterToBrowser(firstWebPage.url) {
        }.openHomeScreen {
        }.openThreeDotMenu {
        }.openLibrary {
        }.openHistory {
            verifyHistoryMenuView()
            verifyVisitedTimeTitle()
            verifyFirstTestPageTitle("Test_Page_1")
            verifyTestPageUrl(firstWebPage.url)
        }
    }

    @Test
    fun deleteHistoryItemTest() {
        val firstWebPage = TestAssetHelper.getGenericAsset(mockWebServer, 1)

        navigationToolbar {
        }.enterURLAndEnterToBrowser(firstWebPage.url) {
        }.openHomeScreen {
        }.openThreeDotMenu {
        }.openLibrary {
        }.openHistory {
            openOverflowMenu()
            clickThreeDotMenuDelete()
            verifyEmptyHistoryView()
        }
    }

    @Test
    fun deleteAllHistoryTest() {
        val firstWebPage = TestAssetHelper.getGenericAsset(mockWebServer, 1)

        navigationToolbar {
        }.enterURLAndEnterToBrowser(firstWebPage.url) {
        }.openHomeScreen {
        }.openThreeDotMenu {
        }.openLibrary {
        }.openHistory {
            clickDeleteHistoryButton()
            verifyDeleteConfirmationMessage()
            confirmDeleteAllHistory()
            verifyEmptyHistoryView()
        }
    }

    @Test
    fun multiSelectionToolbarTest() {
        val firstWebPage = TestAssetHelper.getGenericAsset(mockWebServer, 1)

        navigationToolbar {
        }.enterURLAndEnterToBrowser(firstWebPage.url) {
        }.openHomeScreen {
        }.openThreeDotMenu {
        }.openLibrary {
        }.openHistory {
            longTapSelectItem(firstWebPage.url)
            verifyMultiSelectionCheckmark()
            verifyMultiSelectionCounter()
            verifyShareButton()
            verifyMultiSelectionOverflowMenu()
        }
    }

    @Test
    fun openHistoryInNewTabTest() {
        val firstWebPage = TestAssetHelper.getGenericAsset(mockWebServer, 1)

        navigationToolbar {
        }.enterURLAndEnterToBrowser(firstWebPage.url) {
        }.openHomeScreen {
        }.openThreeDotMenu {
        }.openLibrary {
        }.openHistory {
            longTapSelectItem(firstWebPage.url)
            openMultiSelectionOverflowMenu()
        }.clickOpenNewTab {
            verifyTabTitle("Test_Page_1")
            verifyOpenTabsHeader()
        }
    }

    @Test
    fun openHistoryInPrivateTabTest() {
        val firstWebPage = TestAssetHelper.getGenericAsset(mockWebServer, 1)

        navigationToolbar {
        }.enterURLAndEnterToBrowser(firstWebPage.url) {
        }.openHomeScreen {
        }.openThreeDotMenu {
        }.openLibrary {
        }.openHistory {
            longTapSelectItem(firstWebPage.url)
            openMultiSelectionOverflowMenu()
        }.clickOpenPrivateTab {
            verifyTabTitle("Test_Page_1")
            verifyPrivateSessionHeader()
        }
    }

    @Test
    fun deleteMultipleSelectionTest() {
        val firstWebPage = TestAssetHelper.getGenericAsset(mockWebServer, 1)
        val secondWebPage = TestAssetHelper.getGenericAsset(mockWebServer, 2)

        navigationToolbar {
        }.enterURLAndEnterToBrowser(firstWebPage.url) {
        }.openNavigationToolbar {
        }.enterURLAndEnterToBrowser(secondWebPage.url) {
        }.openHomeScreen {
        }.openThreeDotMenu {
        }.openLibrary {
        }.openHistory {
            longTapSelectItem(firstWebPage.url)
            longTapSelectItem(secondWebPage.url)
            openMultiSelectionOverflowMenu()
            clickMultiSelectionDelete()
            verifyEmptyHistoryView()
        }
    }

    @Test
    fun shareButtonTest() {
        val firstWebPage = TestAssetHelper.getGenericAsset(mockWebServer, 1)

        navigationToolbar {
        }.enterURLAndEnterToBrowser(firstWebPage.url) {
        }.openHomeScreen {
        }.openThreeDotMenu {
        }.openLibrary {
        }.openHistory {
            longTapSelectItem(firstWebPage.url)
            clickShareButton()
            verifyShareScrim()
            verifyShareTabFavicon()
            verifyShareTabTitle()
            verifyShareTabUrl()
        }
    }

    @Test
    fun verifyBackNavigation() {
        homeScreen {
        }.openThreeDotMenu {
        }.openLibrary {
        }.openHistory {
        }.goBack {
            verifyLibraryView()
        }
    }

    @Test
    fun verifyCloseMenu() {
        homeScreen {
        }.openThreeDotMenu {
        }.openLibrary {
        }.openHistory {
        }.closeMenu {
            verifyHomeScreen()
        }
    }
}
