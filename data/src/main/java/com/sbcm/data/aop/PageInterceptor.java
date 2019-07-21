package com.sbcm.data.aop;


import com.sbcm.common.vo.PageVO;
import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.executor.parameter.ParameterHandler;
import org.apache.ibatis.logging.Log;
import org.apache.ibatis.logging.jdbc.ConnectionLogger;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.plugin.*;
import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.session.ResultHandler;
import org.apache.ibatis.session.RowBounds;
import org.apache.ibatis.transaction.Transaction;
import org.springframework.stereotype.Component;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;


/**
 * 
 * <p>
 * 分页查询时把List放入参数page中并返回
 * </p>
 * 
 * @author dixingxing
 * @date 2012-7-12
 */
@Intercepts({ @Signature(type = Executor.class, method = "query", args = {
		MappedStatement.class, Object.class, RowBounds.class,
		ResultHandler.class }) })
@Component
public class PageInterceptor implements Interceptor {
	// 存储所有语句名称
	HashMap<String, String> map_statement = new HashMap<String, String>();
	// 用户提供分页计算条数后缀
	static final String COUNT_ID = "_count";

	/**
	 * 获取所有statement语句的名称
	 * <p>
	 *
	 * @param configuration
	 */
	protected synchronized void initStatementMap(Configuration configuration) {
		if (!map_statement.isEmpty()) {
			return;
		}
		Collection<String> statements = configuration.getMappedStatementNames();
		for (Iterator<String> iter = statements.iterator(); iter.hasNext();) {
			String element = iter.next();
			map_statement.put(element, element);
		}
	}

	/**
	 * 获取数据库连接
	 * <p>
	 *
	 * @param transaction
	 * @param statementLog
	 * @return
	 * @throws SQLException
	 */
	protected Connection getConnection(Transaction transaction, Log statementLog)
			throws SQLException {
		Connection connection = transaction.getConnection();
		if (statementLog.isDebugEnabled()) {
			return ConnectionLogger.newInstance(connection, statementLog, 0);
		} else {
			return connection;
		}
	}

	@Override
	public Object intercept(Invocation invocation) throws Throwable {
		Object parameter = invocation.getArgs()[1];
		PageVO page = seekPage(parameter);
		if (page == null) {
			return invocation.proceed();
		} else {
			return handlePaging(invocation, parameter, page);
		}

	}

	/**
	 * 处理分页的情况
	 * <p>
	 * 
	 * @param invocation
	 * @param parameter
	 * @param page
	 * @throws SQLException
	 */
	@SuppressWarnings("rawtypes")
	protected List handlePaging(Invocation invocation, Object parameter,
			PageVO page) throws Exception {
		MappedStatement mappedStatement = (MappedStatement) invocation
				.getArgs()[0];
		Configuration configuration = mappedStatement.getConfiguration();
		if (map_statement.isEmpty()) {
			initStatementMap(configuration);
		}
		// 设置到page对象
		page.setTotalCount(getTotalSize(invocation, mappedStatement));
		// 查询结果集
		List data = (List) exeQuery(invocation, mappedStatement
				.getConfiguration().getMappedStatement(mappedStatement.getId()));

		return data;
	}

	/**
	 * 根据提供的语句执行查询操作
	 * <p>
	 *
	 * @param invocation
	 * @param query_statement
	 * @return
	 * @throws Exception
	 */
	protected Object exeQuery(Invocation invocation,
			MappedStatement query_statement) throws Exception {
		Object[] args = invocation.getArgs();
		return invocation.getMethod().invoke(invocation.getTarget(),
				new Object[] { query_statement, args[1], args[2], args[3] });
	}

	/**
	 * 获取总记录数量
	 * <p>
	 *
	 * @param configuration
	 * @param mappedStatement
	 * @param sql
	 * @param parameter
	 * @return
	 * @throws SQLException
	 */
	@SuppressWarnings("rawtypes")
	protected Integer getTotalSize(Invocation invocation,
			MappedStatement mappedStatement) throws Exception {

		String count_id = mappedStatement.getId() + COUNT_ID;
		int totalSize = 0;
		if (map_statement.containsKey(count_id)) {
			// 优先查找能统计条数的sql
			List data = (List) exeQuery(invocation, mappedStatement
					.getConfiguration().getMappedStatement(count_id));
			if (data.size() > 0) {
                totalSize = Integer.parseInt(data.get(0).toString());
            }
		}

		return totalSize;
	}

	/**
	 * 拼接查询sql,加入limitint
	 * <p>
	 *
	 * @param sql
	 * @param page
	 */
	protected String getLimitString(String sql, PageVO page) {
		StringBuffer sb = new StringBuffer(sql.length() + 100);
		sb.append(sql);
		sb.append(" limit ").append(page.getStart() - 1).append(",")
				.append(page.getPageSize());
		return sb.toString();
	}

	/**
	 * 拼接获取条数的sql语句
	 * <p>
	 *
	 * @param sqlPrimary
	 */
	protected String getCountSql(String sqlPrimary) {
		String sqlUse = sqlPrimary.replaceAll("[\\s]+", " ");
		String upperString = sqlUse.toUpperCase();
		int order_by = upperString.lastIndexOf(" ORDER BY ");
		if (order_by > -1) {
			sqlUse = sqlUse.substring(0, order_by);
		}
		String[] paramsAndMethod = sqlUse.split("\\s");
		int count = 0;
		int index = 0;
		for (int i = 0; i < paramsAndMethod.length; i++) {
			String upper = paramsAndMethod[i].toUpperCase();
			if (upper.length() == 0) {
				continue;
			}
			if (upper.contains("SELECT")) {
				count++;
			} else if (upper.contains("FROM")) {
				count--;
			}
			if (count == 0) {
				index = i;
				break;
			}
		}
		StringBuilder return_sql = new StringBuilder("SELECT COUNT(1) AS cnt ");
		StringBuilder common_count = new StringBuilder();
		for (int j = index; j < paramsAndMethod.length; j++) {
			common_count.append(" ");
			common_count.append(paramsAndMethod[j]);
		}
		if (upperString.contains(" GROUP BY ")) {
			throw new RuntimeException(
					"不支持group by 分页,请自行提供sql语句以 查询语句+_count结尾.");
		}
		return return_sql.append(common_count).toString();
	}

	/**
	 * 计算总条数
	 * <p>
	 *
	 * @param parameterObj
	 * @param countSql
	 * @param connection
	 * @return
	 */
	protected int getTotalSize(Configuration configuration,
			MappedStatement mappedStatement, BoundSql boundSql,
			String countSql, Connection connection, Object parameter)
			throws SQLException {
		PreparedStatement stmt = null;
		ResultSet rs = null;
		int totalSize = 0;
		try {
			ParameterHandler handler = configuration.newParameterHandler(
					mappedStatement, parameter, boundSql);
			stmt = connection.prepareStatement(countSql);
			handler.setParameters(stmt);
			rs = stmt.executeQuery();
			if (rs.next()) {
				totalSize = rs.getInt(1);
			}
		} catch (SQLException e) {
			throw e;
		} finally {
			if (rs != null) {
				rs.close();
				rs = null;
			}
			if (stmt != null) {
				stmt.close();
				stmt = null;
			}
		}
		return totalSize;
	}

	/**
	 * 寻找page对象
	 * <p>
	 *
	 * @param parameter
	 */
	@SuppressWarnings("rawtypes")
	protected PageVO seekPage(Object parameter) {
		PageVO page = null;
		if (parameter == null) {
			return null;
		}
		if (parameter instanceof PageVO) {
			page = (PageVO) parameter;
		} else if (parameter instanceof Map) {
			Map map = (Map) parameter;
			for (Object arg : map.values()) {
				if (arg instanceof PageVO) {
					page = (PageVO) arg;
				}
			}
		}
		return page;
	}

	@Override
	public Object plugin(Object target) {
		return Plugin.wrap(target, this);
	}

	@Override
	public void setProperties(Properties properties) {
	}
}
