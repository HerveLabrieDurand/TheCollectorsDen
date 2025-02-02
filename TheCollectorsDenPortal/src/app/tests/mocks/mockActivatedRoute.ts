import { of } from 'rxjs';

export const mockActivatedRoute = {
  snapshot: {
    params: {},
    queryParams: {},
  },
  params: of({}),
  queryParams: of({}),
};
