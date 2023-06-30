package com.mss.constant;

/**
 * mapping json建elasticsearch索引常量类
 *
 */
public class PublicCardConstant {

//    建PublicCard索引json语句
    public static final String MAPPING_TEMPLATE = "{\n" +
            "  \"mappings\": {\n" +
            "    \"properties\": {\n" +
            "      \"id\": {\n" +
            "        \"type\": \"integer\"\n" +
            "      },\n" +
            "      \"user_id\": {\n" +
            "        \"type\": \"integer\"\n" +
            "      },\n" +
            "      \"type\": {\n" +
            "        \"type\": \"keyword\"\n" +
            "      },\n" +
            "      \"question\":{\n" +
            "        \"type\": \"text\",\n" +
            "        \"analyzer\": \"ik_max_word\",\n" +
            "        \"search_analyzer\": \"ik_smart\"\n" +
            "      },\n" +
            "      \"answer\":{\n" +
            "        \"type\": \"text\",\n" +
            "        \"analyzer\": \"ik_max_word\",\n" +
            "        \"search_analyzer\": \"ik_smart\"\n" +
            "      },\n" +
            "      \"title\":{\n" +
            "        \"type\": \"text\",\n" +
            "        \"analyzer\": \"ik_max_word\",\n" +
            "        \"search_analyzer\": \"ik_smart\"\n" +
            "      },\n" +
            "      \"note\":{\n" +
            "        \"type\": \"text\",\n" +
            "        \"analyzer\": \"ik_max_word\",\n" +
            "        \"search_analyzer\": \"ik_smart\"\n" +
            "      },\n" +
            "      \"option\":{\n" +
            "        \"type\": \"text\",\n" +
            "        \"search_analyzer\": \"ik_smart\",\n" +
            "        \"index\": false\n" +
            "      },\n" +
            "      \"analysis\":{\n" +
            "        \"type\": \"text\",\n" +
            "        \"analyzer\": \"ik_max_word\",\n" +
            "        \"search_analyzer\": \"ik_smart\"\n" +
            "      },\n" +
            "      \"word\":{\n" +
            "        \"type\": \"text\",\n" +
            "        \"analyzer\": \"ik_max_word\",\n" +
            "        \"search_analyzer\": \"ik_smart\"\n" +
            "      },\n" +
            "      \"interpretation\":{\n" +
            "        \"type\": \"text\",\n" +
            "        \"analyzer\": \"ik_max_word\",\n" +
            "        \"search_analyzer\": \"ik_smart\"\n" +
            "      },\n" +
            "      \"personal_note\":{\n" +
            "        \"type\": \"text\",\n" +
            "        \"analyzer\": \"ik_max_word\",\n" +
            "        \"search_analyzer\": \"ik_smart\"\n" +
            "      },\n" +
            "      \"front\":{\n" +
            "        \"type\": \"text\",\n" +
            "        \"analyzer\": \"ik_max_word\",\n" +
            "        \"search_analyzer\": \"ik_smart\"\n" +
            "      },\n" +
            "      \"back\":{\n" +
            "        \"type\": \"text\",\n" +
            "        \"analyzer\": \"ik_max_word\",\n" +
            "        \"search_analyzer\": \"ik_smart\"\n" +
            "      },\n" +
            "      \"belong_to_box_name\":{\n" +
            "        \"type\": \"text\",\n" +
            "        \"index\": false\n" +
            "      },\n" +
            "      \"belong_to_box_id\":{\n" +
            "        \"type\": \"integer\",\n" +
            "        \"index\": false\n" +
            "      },\n" +
            "      \"create_time\":{\n" +
            "        \"type\": \"date\",\n" +
            "        \"index\": false\n" +
            "      },\n" +
            "      \"update_time\":{\n" +
            "        \"type\": \"date\",\n" +
            "        \"index\": false\n" +
            "      },\n" +
            "      \"last_learn_time\":{\n" +
            "        \"type\": \"date\",\n" +
            "        \"index\": false\n" +
            "      },\n" +
            "      \"learn_count\":{\n" +
            "        \"type\": \"integer\",\n" +
            "        \"index\": false\n" +
            "      },\n" +
            "      \"del_flag\":{\n" +
            "        \"type\": \"integer\",\n" +
            "        \"index\": false\n" +
            "      }\n" +
            "    }\n" +
            "  }\n" +
            "}";

}
