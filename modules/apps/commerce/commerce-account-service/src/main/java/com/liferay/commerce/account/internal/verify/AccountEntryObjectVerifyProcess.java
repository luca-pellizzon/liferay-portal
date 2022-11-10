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

package com.liferay.commerce.account.internal.verify;

import com.liferay.portal.kernel.service.CompanyLocalService;
import com.liferay.portal.kernel.service.UserLocalService;
import com.liferay.portal.kernel.util.LoggingTimer;
import com.liferay.portal.verify.VerifyProcess;
import com.liferay.object.service.ObjectDefinitionLocalService;
import com.liferay.object.service.ObjectFieldLocalService;
import com.liferay.object.model.ObjectDefinition;
import com.liferay.object.model.ObjectField;
import com.liferay.portal.kernel.language.LanguageUtil;
import com.liferay.portal.kernel.util.LocaleUtil;
import com.liferay.object.util.LocalizedMapUtil;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

/**
 * @author Danny Situ
 */
@Component(
	immediate = true,
	property = {
		"initial.deployment=true",
		"verify.process.name=com.liferay.commerce.account.service"
	},
	service = {AccountEntryObjectVerifyProcess.class, VerifyProcess.class}
)
public class AccountEntryObjectVerifyProcess extends VerifyProcess {

	public void verifyAccountEntryObject() throws Exception {
		try (LoggingTimer loggingTimer = new LoggingTimer()) {

			_companyLocalService.forEachCompanyId(
				companyId -> {
					ObjectDefinition objectDefinition = _objectDefinitionLocalService.fetchSystemObjectDefinition("AccountEntry");

					if (objectDefinition == null) {
						throw new Exception("No AccountEntry System Object Definition");
					}

					ObjectField objectField = _objectFieldLocalService.fetchObjectField(objectDefinition.getObjectDefinitionId(), "points");

					if (objectField != null) {
						return;
					}

					_objectFieldLocalService.addOrUpdateSystemObjectField(
						_userLocalService.getDefaultUserId(companyId), objectDefinition.getObjectDefinitionId(), "Integer",
						"points", "AccountEntry", "Integer",
						null, false, false,
						null, LocalizedMapUtil.getLocalizedMap(LanguageUtil.get(LocaleUtil.getDefault(), "Points")), "points",
						false, false);
				});
		}
	}

	@Override
	protected void doVerify() throws Exception {
		verifyAccountEntryObject();
	}

	@Reference
	private CompanyLocalService _companyLocalService;

	@Reference
	private ObjectDefinitionLocalService _objectDefinitionLocalService;

	@Reference
	private ObjectFieldLocalService _objectFieldLocalService;

	@Reference
	private UserLocalService _userLocalService;

}