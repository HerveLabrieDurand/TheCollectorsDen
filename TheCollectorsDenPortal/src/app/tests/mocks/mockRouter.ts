import { of } from 'rxjs';

export const mockRouter = {
  navigate: jest.fn(),
  events: of(), // Observable of router events
  createUrlTree: jest.fn(),
  serializeUrl: jest.fn(),
  config: [], // Router configuration
  url: '/', // Current URL
  routerState: {
    snapshot: {
      root: {
        params: {},
        queryParams: {},
      },
    },
  },
};
