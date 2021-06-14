package cn.gong.easyetl.websystem.util;

import cn.gong.easyetl.common.constant.ConstantPool;
import cn.gong.easyetl.common.constant.DataBaseType;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;

/**
 *
 * @author gongxin
 * @date 2020/9/10
 */
public class SqlQueryBuilder {
    public String table;
    public Set<String> columns;
    private String groupBy;
    private List<OrderBy> orderBy;
    private String having;
    private Integer limit;
    private Integer pageSize;
    public ConditionBuilder condition;
    private DataBaseType dataBaseType;

    public SqlQueryBuilder(DataBaseType dataBaseType, String table, String column) {
        Set<String> columns = new LinkedHashSet<>();
        columns.add(column);
        this.table = table;
        this.columns = columns;
        this.dataBaseType = dataBaseType;
    }

    public SqlQueryBuilder(DataBaseType dataBaseType, String table, String column, ConditionBuilder condition) {
        Set<String> columns = new LinkedHashSet<>();
        columns.add(column);
        this.table = table;
        this.columns = columns;
        this.condition = condition;
        this.dataBaseType = dataBaseType;
    }

    public SqlQueryBuilder(DataBaseType dataBaseType, String table, Set<String> columns) {
        this.table = table;
        this.columns = columns;
        this.dataBaseType = dataBaseType;
    }

    public SqlQueryBuilder(DataBaseType dataBaseType, String table, Set<String> columns, ConditionBuilder condition) {
        this.table = table;
        this.columns = columns;
        this.condition = condition;
        this.dataBaseType = dataBaseType;
    }

    public SqlQueryBuilder() {
    }

    public ConditionBuilder setConditionBuilder(ConditionBuilder conditionBuilder) {
        condition = conditionBuilder;
        return condition;
    }

    public ConditionBuilder newConditionBuilder() {
        condition = new ConditionBuilder(dataBaseType);
        return condition;
    }

    public SqlQueryBuilder groupBy(String field) {
        this.groupBy = dataBaseType.getSymbol() + field + dataBaseType.getSymbol();
        return this;
    }

    public SqlQueryBuilder havingGte(String field, Object data) {
        this.having = dataBaseType.getSymbol() + field + dataBaseType.getSymbol() +" >= " + data;
        return this;
    }

    public SqlQueryBuilder havingGt(String field, Object data) {
        this.having = dataBaseType.getSymbol() + field + dataBaseType.getSymbol() +" > " + data;
        return this;
    }

    public SqlQueryBuilder havingLte(String field, Object data) {
        this.having = dataBaseType.getSymbol() + field + dataBaseType.getSymbol() +" <= " + data;
        return this;
    }

    public SqlQueryBuilder havingLt(String field, Object data) {
        this.having = dataBaseType.getSymbol() + field + dataBaseType.getSymbol() +" < " + data;
        return this;
    }

    public SqlQueryBuilder columns(String field) {
        if (this.columns == null) {
            this.columns = new HashSet<>();
        }
        this.columns.add(field);
        return this;
    }

    public SqlQueryBuilder orderBy(String field, boolean desc) {
        if (this.orderBy == null) {
            this.orderBy = new ArrayList<>();
        }
        field = dataBaseType.getSymbol() + field + dataBaseType.getSymbol();
        orderBy.add(new OrderBy(field, desc));
        return this;
    }

    public SqlQueryBuilder limit(Integer limit) {
        this.limit = limit;
        return this;
    }

    public SqlQueryBuilder page(Integer start, Integer pageSize) {
        this.limit = start;
        this.pageSize = pageSize;
        return this;
    }

    public String get() {
        StringBuilder sb = new StringBuilder("select ");
        int count = 0;
        for (String column : columns) {
            if (column.contains("as") || column.contains("count") || column.contains("distinct") || column.contains("max")) {
                sb.append(column);
            }else {
                sb.append(dataBaseType.getSymbol() + column + dataBaseType.getSymbol());
            }
            if (++count != columns.size()) {
                sb.append(ConstantPool.COMMA);
            }
        }
        sb.append(" from ").append(table).append(" where ").append(ObjectUtils.isNotEmpty(condition) ? condition : " 1 = 1 ");
        if (StringUtils.isNotEmpty(groupBy)) {
            sb.append(" group by ").append(groupBy);
        }
        if (ObjectUtils.isNotEmpty(having)) {
            sb.append(" having ").append(having);
        }
        if (ObjectUtils.isNotEmpty(orderBy)) {
            sb.append(" order by ");
            count = 0;
            for (OrderBy orderByTmp: this.orderBy) {
                sb.append(orderByTmp.getOrderBy());
                if (orderByTmp.getDesc()) {
                    sb.append(" desc ");
                }
                if (++count != this.orderBy.size()) {
                    sb.append(ConstantPool.COMMA);
                }
            }
        }

        if (ObjectUtils.isNotEmpty(limit)) {
            sb.append(" limit ").append(limit);
            if (ObjectUtils.isNotEmpty(pageSize)) {
                sb.append(ConstantPool.COMMA).append(pageSize);
            }
        }
        return sb.toString();
    }

    @Override
    public String toString() {
        return get();
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class OrderBy {
        private String orderBy;
        private Boolean desc;
    }

    /**
     * @description 条件构造器，构造where条件部分
     * @author 倪路
     * @version 1.0
     *
     */
    public static class ConditionBuilder {

        private StringBuilder sb;
        private DataBaseType dataBaseType;

        public ConditionBuilder(DataBaseType dataBaseType) {
            sb = new StringBuilder();
            this.dataBaseType = dataBaseType;
        }

        public ConditionBuilder gt(String field, Object data) {
            sb.append(dataBaseType.getSymbol()).append(field).append(dataBaseType.getSymbol()).append(" > ").append("'").append(data).append("'");
            return this;
        }

        public ConditionBuilder gte(String field, Object data) {
            sb.append(dataBaseType.getSymbol()).append(field).append(dataBaseType.getSymbol()).append(" >= ").append("'").append(data).append("'");
            return this;
        }

        public ConditionBuilder gte(String field, Boolean isNumber, Object data) {
            sb.append(dataBaseType.getSymbol()).append(field).append(dataBaseType.getSymbol()).append(" >= ")
                    .append("toInt64").append("(").append("'").append(data).append("'").append(")");
            return this;
        }

        public ConditionBuilder lt(String field, Object data) {
            sb.append(dataBaseType.getSymbol()).append(field).append(dataBaseType.getSymbol()).append(" < ").append("'").append(data).append("'");
            return this;
        }

        public ConditionBuilder lte(String field, Object data) {
            sb.append(dataBaseType.getSymbol()).append(field).append(dataBaseType.getSymbol()).append(" <= ").append(data);
            return this;
        }

        public ConditionBuilder lte(String field, Boolean isNumber, Object data) {
            sb.append(dataBaseType.getSymbol()).append(field).append(dataBaseType.getSymbol()).append(" <= ")
                    .append("toInt64").append("(").append("'").append(data).append("'").append(")");
            return this;
        }

        public ConditionBuilder mustTrue() {
            sb.append(" (1 = 1) ");
            return this;
        }

        public ConditionBuilder eq(String field, Object data) {
            sb.append(dataBaseType.getSymbol()).append(field).append(dataBaseType.getSymbol()).append(" = ").append("'").append(data).append("'");
            return this;
        }

        public ConditionBuilder neq(String field, Object data) {
            sb.append(dataBaseType.getSymbol()).append(field).append(dataBaseType.getSymbol()).append(" != ").append("'").append(data).append("'");
            return this;
        }

        public ConditionBuilder equals(String field, Object data) {
            sb.append(dataBaseType.getSymbol()).append(field).append(dataBaseType.getSymbol()).append(" = ").append(data);
            return this;
        }

        public ConditionBuilder lBracketOr() {
            sb.append(lBracket()).append(or());
            return this;
        }

        public ConditionBuilder lBracket() {
            sb.append(" ( ");
            return this;
        }

        public ConditionBuilder rBracket() {
            sb.append(" ) ");
            return this;
        }

        public ConditionBuilder rBracketOr() {
            sb.append(or()).append(rBracket());
            return this;
        }

        public ConditionBuilder between(String field, Object start, Object end) {
            sb.append("((").append(dataBaseType.getSymbol()).append(field).append(dataBaseType.getSymbol()).append(" >= ").append(start).append(")").append(" and ").append("(").append(dataBaseType.getSymbol()).append(field).append(dataBaseType.getSymbol()).append(" < ")
                .append(end).append("))");
            return this;
        }

        public ConditionBuilder like(String field, Object data) {
            String newData = data.toString().replace('?', '_').replaceAll("\\*+", "%");
            newData = (String)escapeParam(newData, true);
            sb.append(dataBaseType.getSymbol()).append(field).append(dataBaseType.getSymbol()).append(" like ").append("'").append(newData).append("'");
            return this;
        }

        public ConditionBuilder in(String field, String query) {
            sb.append(dataBaseType.getSymbol()).append(field).append(dataBaseType.getSymbol()).append(" in (").append(query).append(")");
            return this;
        }

        public ConditionBuilder notIn(String field, String query) {
            sb.append(dataBaseType.getSymbol()).append(field).append(dataBaseType.getSymbol()).append(" not in (").append(query).append(")");
            return this;
        }

        public ConditionBuilder combine(ConditionBuilder builder) {
            throw new RuntimeException("Not Support Now!");
        }

        public ConditionBuilder in(String field, boolean isString, Object... data) {
            data = escapeParam(isString, data, false);
            sb.append(dataBaseType.getSymbol()).append(field).append(dataBaseType.getSymbol()).append(" in (");
            int count = 0;
            for (Object dat : data) {
                if (isString) {
                    sb.append("'");
                }
                sb.append(dat);
                if (isString) {
                    sb.append("'");
                }
                if (++count != data.length) {
                    sb.append(ConstantPool.COMMA);
                }
            }
            sb.append(")");
            return this;
        }

        public ConditionBuilder in(String field, Object... data) {
            Object obj = data[0];
            boolean flag = obj instanceof String;
            return in(field, flag, data);
        }

        public ConditionBuilder and() {
            sb.append(" and ");
            return this;
        }
        public ConditionBuilder removeAnd(){
            sb.substring(0,sb.lastIndexOf("."));
            return this;
        }
        public ConditionBuilder limit(int limit){
            sb.append(" limit "+limit);
            return this;
        }

        public ConditionBuilder not() {
            sb.append(" not ");
            return this;
        }

        public ConditionBuilder or() {
            sb.append(" or ");
            return this;
        }

        /**
         *  兼容模糊查询处理
         * @author gongxin
         * @date 2019/12/24
         */
        private static Object escapeParam(Object param, boolean isLike) {
            if (param == null) {
                return param;
            }
            return escapeParam(true, new Object[] {param}, isLike)[0];
        }

        private static Object[] escapeParam(boolean isString, Object[] params, boolean isLike) {
            if (params == null || !isString) {
                return params;
            }
            String[] result = new String[params.length];
            for (int i = 0; i < params.length; i++) {
                String str = (String)params[i];
                str = StringUtils.replace(str, "'", "\\'");
                str = StringUtils.replace(str, ";", "\\;");
                str = StringUtils.replace(str, "-", "\\-");
                str = StringUtils.replace(str, "_", "\\_");
                str = StringUtils.replace(str, ".", "\\.");
                str = StringUtils.replace(str, "*", "\\*");
                str = StringUtils.replace(str, "?", "\\?");
                str = StringUtils.replace(str, "@", "\\@");
                if (!isLike) {
                    str = StringUtils.replace(str, "%", "\\%");
                }
                result[i] = str;
            }
            return result;
        }

        @Override
        public String toString() {
            return sb.toString();
        }
    }
}
