## codegen

codegen是一款代码生成器。可以使用codegen批量生成java或其他语言的源代码、API文档等。

## 项目结构
codegen-core提供了底层支持，crud-gen基于codegen-core模块，是codegen-core的一个应用，可以一建生成crud代码。后续会根据自己在工作中的需要，添加更多模块。

## codegen-core
核心是Generator、DataSource、TemplateSource和Receiver。

DataSource和TemplateSource分别提供数据和模板，由Generator将数据与模板合并生成Code，Receiver接收Code，执行下一步操作，如写入代码文件。

DataSource、TemplateSource和Receiver都可以自由扩展和搭配，以满足不同的需求。

此外提供了一个拦截器即Interceptor接口，可以通过向Generator添加Interceptor来完成某些操作，例如日志打印、动态转换和添加数据等等。

## crud-gen
crud-gen是codegen-core的一个应用，配置好数据库连接、代码目录等信息后，可一键生成java项目的CRUD源代码。需要你手动配置的项目在**application-config.yml**中，请不要修改**application.yml**（除非你有特殊需要）。

## 模板引擎

内置的模板引擎是Velocity，可以通过实现Engine接口来方便的扩展其他模板引擎。

## 数据来源
数据来源可以是json文件、CSV文件，还可以**是数据库**。codegen会根据配置的数据库连接来自动解析数据库元数据，最后与模板结合生成相应代码文件。
