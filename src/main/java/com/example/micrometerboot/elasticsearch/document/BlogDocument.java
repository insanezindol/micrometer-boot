package com.example.micrometerboot.elasticsearch.document;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;
import org.springframework.data.elasticsearch.annotations.Setting;

import javax.persistence.Id;
import javax.persistence.Version;

@Getter
@Setter
@NoArgsConstructor
@Setting(settingPath = "elastic/common-mapping.json")
@Document(indexName = "blog", createIndex = false)
public class BlogDocument {

    @Id
    private String id;

    @Field(type = FieldType.Keyword)
    private String type;

    @Field(type = FieldType.Keyword)
    private String title;

    @Field(type = FieldType.Keyword)
    private String contents;

    @Version
    private int version;

    // 현재일시
    @Field(type = FieldType.Date)
    private String logDate;

}
