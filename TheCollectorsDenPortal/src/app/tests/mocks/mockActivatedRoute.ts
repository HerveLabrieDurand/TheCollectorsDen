import { convertToParamMap } from '@angular/router';
import { of } from 'rxjs';

export const mockActivatedRoute = {
  snapshot: {
    params: {},
    queryParams: {},
    queryParamMap: convertToParamMap({}),
  },
  params: of({}),
  queryParams: of({}),
  queryParamMap: of(convertToParamMap({})),
};
