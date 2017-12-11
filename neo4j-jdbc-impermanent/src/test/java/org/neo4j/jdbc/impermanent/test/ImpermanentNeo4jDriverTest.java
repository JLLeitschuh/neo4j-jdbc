package org.neo4j.jdbc.impermanent.test; /**
 * Copyright (c) 2016 LARUS Business Automation [http://www.larus-ba.it]
 * <p>
 * This file is part of the "LARUS Integration Framework for Neo4j".
 * <p>
 * The "LARUS Integration Framework for Neo4j" is licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * <p>
 * Created on 05/12/17
 */

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.neo4j.jdbc.Neo4jDriver;
import org.neo4j.jdbc.impermanent.ImpermanentNeo4jDriver;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Properties;

import static junit.framework.TestCase.assertFalse;
import static org.junit.Assert.*;

/**
 * @author Gianmarco Laggia @ Larus B.A.
 * @since 3.2.0
 */
public class ImpermanentNeo4jDriverTest {
	@Rule
	public ExpectedException expectedEx = ExpectedException.none();

	private static final String COMPLETE_VALID_URL = "jdbc:neo4j:mem";

	/*------------------------------*/
	/*           connect            */
	/*------------------------------*/

	@Test public void shouldConnectCreateConnection() throws SQLException {
		Neo4jDriver driver = new ImpermanentNeo4jDriver();
		Connection connection = driver.connect(COMPLETE_VALID_URL, null);
		assertNotNull(connection);
	}

	@Test public void shouldConnectCreateConnectionWithNoAuthTokenWithPropertiesObjectWithoutUserAndPassword() throws SQLException {
		Properties properties = new Properties();
		properties.put("test", "TEST_VALUE");

		Neo4jDriver driver = new ImpermanentNeo4jDriver();
		Connection connection = driver.connect(COMPLETE_VALID_URL, properties);
		assertNotNull(connection);
	}

	@Test public void shouldCreateMultipleInMemoryConnectionToDifferentDatabases() throws SQLException {
		Neo4jDriver driver = new ImpermanentNeo4jDriver();
		assertNotNull(driver.connect(COMPLETE_VALID_URL + ":one", null));
		assertNotNull(driver.connect(COMPLETE_VALID_URL + ":two", null));
		assertNotNull(driver.connect(COMPLETE_VALID_URL + ":three", null));
	}

	@Test public void shouldConnectReturnNullIfUrlNotValid() throws SQLException {
		Neo4jDriver driver = new ImpermanentNeo4jDriver();
		assertNull(driver.connect("jdbc:neo4j:http://localhost:7474", null));
		assertNull(driver.connect("bolt://localhost:7474", null));
		assertNull(driver.connect("jdbcbolt://localhost:7474", null));
		assertNull(driver.connect("jdbc:mysql://localhost:3306/sakila", null));
		assertNull(driver.connect("jdbc:neo4j:bolt://localhost:3306", null));
	}

	@Test public void shouldConnectThrowExceptionOnNullURL() throws SQLException {
		expectedEx.expect(SQLException.class);

		Neo4jDriver driver = new ImpermanentNeo4jDriver();
		driver.connect(null, null);
	}

	/*------------------------------*/
	/*          acceptsURL          */
	/*------------------------------*/
	@Test public void shouldAcceptURLOK() throws NoSuchFieldException, IllegalAccessException, ClassNotFoundException, SQLException {
		Neo4jDriver driver = new ImpermanentNeo4jDriver();
		assertTrue(driver.acceptsURL("jdbc:neo4j:mem"));
		assertTrue(driver.acceptsURL("jdbc:neo4j:mem:name"));
		assertTrue(driver.acceptsURL("jdbc:neo4j:mem:othername"));
	}

	@Test public void shouldAcceptURLKO() throws NoSuchFieldException, IllegalAccessException, ClassNotFoundException, SQLException {
		Neo4jDriver driver = new ImpermanentNeo4jDriver();
		assertFalse(driver.acceptsURL("jdbc:neo4j:http://localhost:7474"));
		assertFalse(driver.acceptsURL("jdbc:file://192.168.0.1:7474"));
		assertFalse(driver.acceptsURL("bolt://localhost:7474"));
		assertFalse(driver.acceptsURL("jdbc:neo4j:bolt://localhost:7474"));
		assertFalse(driver.acceptsURL("jdbc:neo4j:bolt://192.168.0.1:7474"));
		assertFalse(driver.acceptsURL("jdbc:neo4j:bolt://localhost:8080"));
	}

	@Test public void shouldThrowException() throws NoSuchFieldException, IllegalAccessException, ClassNotFoundException, SQLException {
		expectedEx.expect(SQLException.class);

		Neo4jDriver driver = new ImpermanentNeo4jDriver();
		assertFalse(driver.acceptsURL(null));
	}
}
