package ${packName};
 
import javax.persistence.*;
import com.alibaba.fastjson.JSON;
import com.ydtf.soam.system.entity.EntityBase;
<#if entityDb == "MONGODB">
import org.springframework.data.mongodb.core.mapping.Document;
</#if>

<#if importDate == true>
import java.util.Date;
</#if>
<#if importTime == true>
import java.sql.Timestamp;
</#if>
<#if importBigsem == true>
import java.math.BigDecimal;
</#if>

/**
 * <p>实体类</p>
 * <p>Table: ${tableName}</p>
 * 
 * @since ${date}
 *
 */

<#if entityDb == "MONGODB">
@Document
<#else>
@Entity
</#if>
public class ${className} extends EntityBase{
	
	<#if colum ?exists>
	<#list colum as col>
	<#if col.isPrimaryKey == "true">
	@Id
	</#if>
	<#if col.isGenare == "true">
	@GeneratedValue(strategy = GenerationType.AUTO)
	</#if>
	@Column(name="${col.properName}", unique=${col.isUnique}, nullable=${col.isNullable}${col.length}${col.decimalDigits})
	private ${col.columnType} ${col.columnName};
	
	</#list>
	</#if>
	
	<#if colum ?exists>
	<#list colum as col>
	public void set${col.method}(${col.columnType} ${col.columnName}) {
		this.${col.columnName} = ${col.columnName};
	}
	
	public ${col.columnType} get${col.method}() {
		return  ${col.columnName};
	}
	
	</#list>
	</#if>
	
}