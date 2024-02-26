// 时间戳：1637244864707
/* 时间戳转换为时间 */
export function timestampToTime(timestamp: string | null): string {
    if (timestamp === null) {
        return '';
    }

    const date = new Date(timestamp);

    const Y = date.getFullYear() + '-';
    const M = (date.getMonth() + 1 < 10 ? '0' + (date.getMonth() + 1) : date.getMonth() + 1) + '-';
    const D = (date.getDate() < 10 ? '0' + date.getDate() : date.getDate()) + ' ';
    const h = (date.getHours() < 10 ? '0' + date.getHours() : date.getHours()) + ':';
    const m = (date.getMinutes() < 10 ? '0' + date.getMinutes() : date.getMinutes()) + ':';
    const s = date.getSeconds() < 10 ? '0' + date.getSeconds() : date.getSeconds();

    return Y + M + D + h + m + s;
}
export class DateTimeConverter {
    static toISO8601(dateString: string | number | Date) {
      const date = new Date(dateString);
      return date.toISOString();
    }
  
    static toCustomFormat(dateString: string | number | Date) {
      const date = new Date(dateString);
      const year = date.getFullYear();
      const month = date.getMonth() + 1;
      const day = date.getDate();
      const hours = date.getHours();
      const minutes = date.getMinutes();
      return `${year}/${month}/${day} ${hours}:${minutes}`;
    }
    static toCustomMinuteFormat(dateString: string | number | Date) {
      const date = new Date(dateString);
      const year = date.getFullYear();
      const month = date.getMonth() + 1;
      const day = date.getDate();
      const hours = date.getHours();
      const minutes = date.getMinutes();
      return `${year}/${month}/${day} ${hours}:${minutes}`;
    }
    static toCustomDayFormat(dateString: string | number | Date) {
      const date = new Date(dateString);
      const year = date.getFullYear();
      const month = date.getMonth() + 1;
      const day = date.getDate();

      return `${year}/${month}/${day} `;
    }
    static toCustomMonthDayFormat(dateString: string | number | Date) {
      const date = new Date(dateString);
      const month = date.getMonth() + 1;
      const day = date.getDate();

      return `${month}/${day} `;
    }
    
    static isSameDay(date1: Date, date2: Date) {
      return (
        date1.getFullYear() === date2.getFullYear() &&
        date1.getMonth() === date2.getMonth() &&
        date1.getDate() === date2.getDate()
      );
    }
  
  
  }