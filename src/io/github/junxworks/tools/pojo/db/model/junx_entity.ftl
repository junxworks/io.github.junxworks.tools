package ${package};

import java.io.Serializable;
import javax.persistence.*;
<#if (table.hasDateColumn)>
import java.util.Date;
</#if>
<#if (table.hasBigDecimalColumn)>
import java.math.BigDecimal;
</#if>

/**
 * <p>实体类</p>
 * <p>Table: ${table.tableName} - ${table.remarks}</p>
 *
 * @since ${.now}
 */
 
@Entity
public class ${table.className}  implements Serializable{
    private static final long serialVersionUID = 1L;
	
<#list table.primaryKeys as key>
    @Id
    <#if (key.autoIncrement)>
	@GeneratedValue
	</#if>
    @Column(name="${key.columnName}", unique=${key.unique?c}, nullable=${key.nullable?c}, length=${key.size})
    private ${key.javaType} ${key.javaProperty};
    
</#list>
<#list table.baseColumns as column>
	<#if (column.autoIncrement)>
	@GeneratedValue
	</#if>
    @Column(name="${column.columnName}", unique=${column.unique?c}, nullable=${column.nullable?c}, length=${column.size})
	private ${column.javaType} ${column.javaProperty};
	
</#list>

<#list table.primaryKeys as key>
    public ${key.javaType} ${key.getterMethodCamelName}(){
        return this.${key.javaProperty};
    }

    public void ${key.setterMethodCamelName}(${key.javaType} ${key.javaProperty}){
        this.${key.javaProperty} = ${key.javaProperty};
    }
</#list>

<#list table.baseColumns as column>
    public ${column.javaType} ${column.getterMethodCamelName}(){
        return this.${column.javaProperty};
    }

    public void ${column.setterMethodCamelName}( ${column.javaType} ${column.javaProperty} ){
        this.${column.javaProperty} = ${column.javaProperty};
    }
</#list>
}