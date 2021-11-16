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

package com.liferay.commerce.inventory.internal.upgrade.v3_0_0;

import com.liferay.commerce.inventory.internal.upgrade.v3_0_0.util.CommerceInventoryWarehouseTable;
import com.liferay.petra.string.StringPool;
import com.liferay.portal.kernel.upgrade.UpgradeProcess;
import com.liferay.portal.kernel.upgrade.util.UpgradeProcessUtil;
import com.liferay.portal.kernel.util.HashMapBuilder;
import com.liferay.portal.kernel.util.LocalizationUtil;
import com.liferay.portal.kernel.util.Validator;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;

/**
 * @author Andrea Sbarra
 */
public class CommerceInventoryWarehouseUpgradeProcess extends UpgradeProcess {

	@Override
	protected void doUpgrade() throws Exception {
		alter(
			CommerceInventoryWarehouseTable.class,
			new AlterColumnType("name", "STRING null"),
			new AlterColumnType("description", "STRING null"));

		try (PreparedStatement preparedStatement = connection.prepareStatement(
				"update CIWarehouse set name = ?, description = ? where " +
					"CIWarehouseId = ?");
			Statement s = connection.createStatement();
			ResultSet resultSet = s.executeQuery(
				"select CIWarehouseId, companyId, name, description from " +
					"CIWarehouse")) {

			while (resultSet.next()) {
				String languageId = UpgradeProcessUtil.getDefaultLanguageId(
					resultSet.getLong("companyId"));

				preparedStatement.setString(
					1,
					LocalizationUtil.getXml(
						HashMapBuilder.put(
							languageId, resultSet.getString("name")
						).build(),
						languageId, "name"));

				String description = resultSet.getString("description");

				if (!Validator.isBlank(description)) {
					preparedStatement.setString(
						2,
						LocalizationUtil.getXml(
							HashMapBuilder.put(
								languageId, description
							).build(),
							languageId, "description"));
				}
				else {
					preparedStatement.setString(
						2,
						LocalizationUtil.getXml(
							HashMapBuilder.put(
								languageId, StringPool.BLANK
							).build(),
							languageId, "description"));
				}

				preparedStatement.setLong(
					3, resultSet.getLong("CIWarehouseId"));

				preparedStatement.execute();
			}
		}
	}

}