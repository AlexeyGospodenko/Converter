Программа принимает имя входного текстового файла и файл с описанием правил трансформации. Результат выдается в stdout
Возможный вариант вызова:

```
java -jar converter.jar -input input.txt -rules rules
```

В файле rules находятся regexp-шаблоны поиска и замены, например

```
Вася->Петя
[А-Я][а-я]+а->Анжелика
```

- [x] Использование maven в качестве build system
- [x] Использование logging framework'а (log4j, slf4j)
- [x] Использование testing framework'а (testng, junit)
- [x] Использование репозитория (github, bitbucket)

- [ ] Возможность указать url для входного файла, например
  \>java -jar converter.jar -input http://lib.ru/INOOLD/DODE/r_kure.txt -rules rules

- [ ] Возможность чтения из stdin, например
  \>ls -alh | java -jar converter.jar -rules rules

- [ ] Возможность параллельной обработки нескольких файлов

