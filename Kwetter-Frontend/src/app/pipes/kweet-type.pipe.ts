import { Pipe, PipeTransform } from '@angular/core';

@Pipe({
  name: 'kweetType'
})
export class KweetTypePipe implements PipeTransform {

  transform(value: number): string {
    switch (value) {
      case 1:
        return 'Message';
      case 2:
        return 'Reply';
      case 3:
        return 'Remessage';
      default:
        return 'Unknown';
    }
  }
}
