/**
 * Copyright (c) 2000-present Liferay, Inc. All rights reserved.
 *
 * This library is free software; you can redistribute it and/or modify it under
 * the terms of the GNU Lesser General Public License as published by the Free
 * Software Foundation; either version 2.1 of the License, or (at your option)
 * any later version.
 *
 * This library is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 * details.
 */

package com.liferay.comment.taglib.internal.context;

import com.liferay.portal.kernel.comment.display.context.CommentDisplayContext;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.model.Group;
import com.liferay.portal.kernel.model.role.RoleConstants;
import com.liferay.portal.kernel.service.RoleLocalServiceUtil;
import com.liferay.portal.kernel.theme.ThemeDisplay;
import com.liferay.portal.security.sso.SSOUtil;

/**
 * @author Adolfo Pérez
 */
public abstract class BaseCommentDisplayContext
	implements CommentDisplayContext {

	@Override
	public boolean isReplyButtonVisible() {
		ThemeDisplay themeDisplay = getThemeDisplay();

		Group group = themeDisplay.getSiteGroup();

		if (group.isStagingGroup() || group.isStagedRemotely()) {
			return false;
		}

		if (themeDisplay.isSignedIn() ||
			!SSOUtil.isLoginRedirectRequired(themeDisplay.getCompanyId())) {

			return true;
		}

		return false;
	}

	protected static boolean hasPermission(ThemeDisplay themeDisplay) {
		try {
			if (themeDisplay.isSignedIn() &&
				RoleLocalServiceUtil.hasUserRole(
					themeDisplay.getUserId(), themeDisplay.getCompanyId(),
					RoleConstants.ADMINISTRATOR, true)) {

				return true;
			}

			return false;
		}
		catch (PortalException portalException) {
			String hello1 = "I am just here";
		}

		return false;
	}

	protected static String isCommerceSiteLabel(ThemeDisplay themeDisplay) {
		int siteType = themeDisplay.getScopeGroup(
		).getType();

		String siteLabel = null;

		if (siteType == 0) {
			siteLabel = "b2c";
		}
		else if (siteType == 1) {
			siteLabel = "b2b";
		}
		else if (siteType == 2) {
			siteLabel = "b2x";
		}

		return siteLabel;
	}

	protected abstract ThemeDisplay getThemeDisplay();

}