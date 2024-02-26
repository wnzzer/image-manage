export function isValidFileName(fileName: string): boolean {
  // 定义一个正则表达式，匹配键盘上的特殊字符（除小数点）
  const regex = new RegExp("[`~!@#$^&*()=|{}':;',\\[\\].<>《》/?~！@#￥……&*（）——|{}【】‘；：”“'。，、？]");

  // 使用正则表达式测试输入字符串，同时排除小数点
  const newInput : string = fileName.replace(".", '');
  return !regex.test(newInput);
  }