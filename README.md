# Rich Translatable Text

**Rich Translatable Text 的 github仓库**

## 项目链接
#### [CurseForge](https://www.curseforge.com/minecraft/mc-mods/rich-translatable-text) - https://www.curseforge.com/minecraft/mc-mods/rich-translatable-text


## 效果展示:
#### [RTTCommand.java](src/main/java/xu_mod/rich_translatable_text/Command/RTTCommand.java)
```
  commandContext.getSource().sendMessage(Text.translatable("text.rich_translatable_text.test_lang"));
```

#### [/rich_lang/zh_cn.json](src/main/resources/assets/rich_translatable_text/lang/zh_cn.json)
```json
{
  "text.rich_translatable_text.test_lang": [
    {"text": "这是一个测试文本", "color": "#0000FF"}, {"text": "(模组Lang)", "color": "#00FF00"}
  ]
}
```

![最终渲染](README_RES/TestLang.png)


## 额外工具 ExtraTools
- [RichLang2Lang,py](Tools/RichLang2Lang.py) - 将rich_lang转化为lang  
使用方法 把RichLang2Lang,py复制到rich_lang同级目录下，然后运行RichLang2Lang.py即可  
!! 不支持 `[{"translatable": "xxx"}]` 的格式 会自动跳过 !!  
!! 注意：RichLang2Lang.py会自动覆盖原lang文件，请备份lang文件夹后使用 !!


### 其他版本的Minecraft支持将在 1.20.1版本更新完毕(Bug修的差不多 + 无新功能要添加) 后添加
如果需要其他版本的支持，请提交issue. 我会尽快编译一个临时版本 (不会添加后续更新)  
